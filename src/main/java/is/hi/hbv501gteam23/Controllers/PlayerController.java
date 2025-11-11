package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto.PlayerResponse;
import is.hi.hbv501gteam23.Services.Interfaces.MetadataService;
import is.hi.hbv501gteam23.Services.Interfaces.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller that exposes read/write operations for {@link Player} resources.
 * Base path is /players
 */
@Tag(name = "Player", description = "Player management")
@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;
    private final MetadataService metadataService;

    /**
     *
     * @param name
     * @param teamId
     * @param teamName
     * @param country
     * @param isActive
     * @param sortBy
     * @param sortDir
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    @Operation(
            summary = "Get all players with optional filters",
            description = "Retrieve all players or filter by name, teamId, teamName, country, or active status. Supports sorting and pagination."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Players successfully fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid query parameters")
    })
    public ResponseEntity<List<PlayerDto.PlayerResponse>> listPlayers(
            @Parameter(description = "Filter by player name") @RequestParam(required = false) String name,
            @Parameter(description = "Filter by team ID") @RequestParam(required = false) Long teamId,
            @Parameter(description = "Filter by team name") @RequestParam(required = false) String teamName,
            @Parameter(description = "Filter by country code. See valid country codes via the /metadata/countries endpoint")
                @RequestParam(required = false) String country,
            @Parameter(description = "Filter by active status") @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "Field to sort by (default = 'name')") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction: ASC or DESC (default = ASC)") @RequestParam(defaultValue = "ASC") String sortDir,
            @Parameter(description = "Page number, 0-indexed") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size
    ) {
        if (country != null) {
            boolean validCountry = metadataService.getAllCountries().stream()
                    .anyMatch(c -> c.code().equalsIgnoreCase(country));
            if (!validCountry) {
                return ResponseEntity
                        .badRequest()
                        .body(Collections.emptyList());
            }
        }

        List<Player> players = playerService.findPlayers(
                name, teamId, teamName, country, isActive, sortBy, sortDir, page, size
        );

        List<PlayerDto.PlayerResponse> response = players.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a single player by id.
     *
     * <p>
     *      This method retrieves a {@link Player} entity from the database with a specific identifier.
     * </p>
     * @param id the id of the player to be retrieved.
     * @return the player mapped to a {@link PlayerResponse}
     * @throws jakarta.persistence.EntityNotFoundException if the player is not found
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get player by ID",
            description = "Retrieve a single player by their unique identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Player successfully fetched"),
            @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public PlayerResponse getPlayerById(
            @Parameter(description = "ID of the player to retrieve") @PathVariable Long id) {
        return toResponse(playerService.getPlayerById(id));
    }

    /**
     * Creates a new player. Only for admins.
     * @param body the player data
     * @return the new player
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a player")
    @ApiResponse(responseCode = "200", description = "Player successfully created")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PlayerDto.PlayerResponse> createPlayer(@RequestBody PlayerDto.CreatePlayerRequest body) {
        Player created = playerService.createPlayer(body);
        return ResponseEntity.created(URI.create("/players" + created.getId())).body(toResponse(created));
    }

    /**
     * Modifies an existing player. Only for admins.
     * @param id the player's id
     * @param body the data that should be modified
     * @return the updated player
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Modify a player")
    @ApiResponse(responseCode = "200", description = "Player successfully modified")
    public ResponseEntity<PlayerResponse> patchPlayer(@PathVariable Long id, @RequestBody PlayerDto.PatchPlayerRequest body) {
        Player updated = playerService.patchPlayer(id, body);
        return ResponseEntity.ok(toResponse(updated));
    }

    /**

    /**
     * Maps a {@link Player} entity to a {@link PlayerResponse} DTO.
     * @param p player entity
     * @return mapped {@link PlayerResponse}
     */
    private PlayerResponse toResponse(Player p) {
        return new PlayerResponse(
                p.getId(),
                p.getName(),
                p.isActive(),
                p.getPosition(),
                p.getGoals(),
                p.getCountry(),
                p.getDateOfBirth(),
                p.getTeam() != null ? p.getTeam().getId()   : null,
                p.getTeam() != null ? p.getTeam().getName() : null
        );
    }
}
