package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto.PlayerResponse;
import is.hi.hbv501gteam23.Services.Interfaces.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller that exposes read/write operations for {@link Player} resources.
 * Base path is /players
 */
@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    /**
     * Retrieves all players.
     * <p>
     *      This method retrieves all {@link Player} entities from the database
     * </p>
     * @return list of players mapped to {@link PlayerResponse}
     */
    @GetMapping
    @Operation(summary = "List all players")
    @ApiResponse(responseCode = "200", description = "Player list successfully fetched")
    public List<PlayerResponse> getAllPlayers() {
        return playerService.getAllPlayers()
                .stream().map(this::toResponse).toList();
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
    @Operation(summary = "Get player by ID")
    @ApiResponse(responseCode = "200", description = "Player successfully fetched")
    public PlayerResponse getPlayerById(@PathVariable Long id) {
        return toResponse(playerService.getPlayerById(id));
    }

    /**
     * Retrieves a player by name
     *
     * <p>
     *     This method retrieves a {@link Player} entity from the database with a specific name.
     * </p>
     * @param name the name of the player in database
     * @return the player mapped to a {@link PlayerResponse}
     */
    @GetMapping(params = "name")
    @Operation(summary = "Get player by name")
    @ApiResponse(responseCode = "200", description = "Search results retrieved")
    public PlayerResponse searchPlayersByName(@RequestParam String name) {
        return toResponse(playerService.searchPlayersByName(name));
    }

    /**
     * Retrieves all players by team name
     * <p>
     *      This method retrieves all {@link Player} entities from the database with a specific name.
     * </p>
     * @param teamName the name of the team in database
     * @return list of players mapped to {@link PlayerResponse}
     */
    @GetMapping(params = "team")
    @Operation(summary = "List all players in team by team name")
    @ApiResponse(responseCode = "200", description = "List successfully fetched")
    public List<PlayerResponse> getAllPlayersByTeamName(@RequestParam String teamName) {
        return playerService.getByTeamName(teamName)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Retrieves all players by team identifier
     *
     * <p>
     *     This method retrieves all {@link Player} entities with a specific team.
     * </p>
     * @param teamId the identifier of the team in database
     * @return list of players mapped to {@link PlayerResponse}
     */
    @GetMapping("/team/{teamId}")
    @Operation(summary = "List all players in team by team ID")
    @ApiResponse(responseCode = "200", description = "List successfully fetched")
    public List<PlayerDto.PlayerResponse> getByVenueId(@PathVariable("teamId") Long teamId) {
        return playerService.getByTeamId(teamId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     *
     * @param isActive
     * @return
     */
    @GetMapping("/isActive={isActive}")
    @Operation(summary = "List all active players")
    @ApiResponse(responseCode = "200", description = "List successfully fetched")
    public List<PlayerDto.PlayerResponse> getActivePlayers(@PathVariable("isActive") Boolean isActive) {
        return playerService.getActivePlayers(isActive)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     *
     * @param country
     * @return
     */
    @GetMapping("/country={country}")
    @Operation(summary = "List all players from specific country")
    @ApiResponse(responseCode = "200", description = "List successfully fetched")
    public List<PlayerResponse> getPlayerByCountry(@PathVariable String country) {
        return playerService.findPlayerByCountry(country.toUpperCase())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Creates a new player. Only for admins.
     * @param body the player data
     * @return the new player
     */
    @Operation(summary = "Create a player")
    @ApiResponse(responseCode = "200", description = "Player successfully created")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
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
