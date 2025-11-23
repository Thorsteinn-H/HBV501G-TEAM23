package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto.PlayerResponse;
import is.hi.hbv501gteam23.Services.Interfaces.MetadataService;
import is.hi.hbv501gteam23.Services.Interfaces.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
@Tag(name = "Player")
@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;
    private final MetadataService metadataService;

    /**
     * Lists players using optional filters, with sorting and pagination.
     * <p>
     * All filter parameters are optional; when {@code null}, they are ignored.
     * If a country is provided, it must be one of the configured countries from {@link MetadataService},
     * otherwise a 400 (Bad Request) is returned with an empty list.
     *
     * @param filter optional filter parameters
     * @return {@link ResponseEntity} with status 200 (OK) containing a list of {@link PlayerResponse},
     * or 400 (Bad Request) with an empty list if the country is invalid
     */
    @GetMapping
    @Operation(
        summary = "List players",
        description = "List all players or use optional filters and sorting."
    )
    public ResponseEntity<List<PlayerDto.PlayerResponse>> listPlayers(
            @ParameterObject @ModelAttribute PlayerDto.PlayerFilter filter
    ) {
        String country = filter != null ? filter.country() : null;
        if (country != null) {
            boolean validCountry = metadataService.getAllCountries().stream()
                .anyMatch(c -> c.value().equalsIgnoreCase(country));
            if (!validCountry) {
                return ResponseEntity
                    .badRequest()
                    .body(Collections.emptyList());
            }
        }

        List<Player> players = playerService.findPlayers(filter);

        List<PlayerDto.PlayerResponse> response = players.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a single player by ID.
     * @param id the ID of the player to find
     * @return the player
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get player by ID", description = "Get a player by their ID.")
    public PlayerResponse getPlayerById(@Parameter @PathVariable Long id) {
        return toResponse(playerService.getPlayerById(id));
    }

    /**
     * Creates a new player. Admin only.
     *
     * @param body the player data to create
     * @return {@link ResponseEntity} with status 201 (CREATED) containing the created player
     * mapped to {@link PlayerResponse} and a {@code Location} header pointing to
     * /players/{id}
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a player")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PlayerDto.PlayerResponse> createPlayer(@RequestBody PlayerDto.CreatePlayerRequest body) {
        Player created = playerService.createPlayer(body);
        return ResponseEntity.created(URI.create("/players/" + created.getId())).body(toResponse(created));
    }

    /**
     * Partially updates an existing player. Admin only.
     *
     * @param id   the player's ID
     * @param body the data that should be modified
     * @return {@link ResponseEntity} with status 200 (OK) containing the updated player
     * mapped to {@link PlayerResponse}
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Modify a player")
    public ResponseEntity<PlayerResponse> patchPlayer(@PathVariable Long id, @RequestBody PlayerDto.PatchPlayerRequest body) {
        Player updated = playerService.patchPlayer(id, body);
        return ResponseEntity.ok(toResponse(updated));
    }

    /**
     * Maps a {@link Player} entity to a {@link PlayerResponse} DTO.
     * @param p player entity to map
     * @return mapped {@link PlayerResponse}
     */
    private PlayerResponse toResponse(Player p) {
        return new PlayerResponse(
                p.getId(),
                p.getName(),
                p.getDateOfBirth(),
                p.getGender(),
                p.getCountry() != null ? p.getCountry().getCode() : null,
                p.getTeam() != null ? p.getTeam().getId()   : null,
                p.getTeam() != null ? p.getTeam().getName() : null,
                p.getPosition(),
                p.getGoals(),
                p.isActive()
        );
    }
}
