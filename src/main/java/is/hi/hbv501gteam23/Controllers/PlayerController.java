package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto.CreatePlayerRequest;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto.PlayerResponse;
import is.hi.hbv501gteam23.Services.Interfaces.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller that exposes read/write operations for {@link Player} resources.
 * Base path is api/players
 * <p>Other endpoints (search, create, update, delete) support administration and UX helpers.</p>
 */
@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    //UC7
    /**
     * Lists all players.
     * @return list of players mapped to {@link PlayerResponse} DTOs
     */
    @GetMapping
    public List<PlayerResponse> getAllPlayers() {
        return playerService.getAllPlayers()
                .stream().map(this::toResponse).toList();
    }

    //UC8
    /**
     * Retrieves a single player by id.
     * @param id database identifier of the player
     * @return the player mapped to a {@link PlayerResponse} DTO
     * @throws jakarta.persistence.EntityNotFoundException if the player is not found
     */
    @GetMapping("/{id}")
    public PlayerResponse getPlayerById(@PathVariable Long id) {
        return toResponse(playerService.getPlayerById(id));
    }

    //optional not UC8
    /**
     * Searches players by name.
     * @param name query substring to match against player names
     * @return list of matching {@link Player} entities
     */
    @GetMapping("/name={name}")
    public PlayerResponse searchPlayersByName(@PathVariable("name") String name) {
        return toResponse(playerService.searchPlayersByName(name));
    }

    // Start/part of Use case 1 doesn't work yet.
    /**
     * Creates a new player.
     * @param body request payload containing player properties and target {@code teamId}
     * @return a 201 response with the created {@link PlayerResponse} body
     * @throws jakarta.persistence.EntityNotFoundException if {@code teamId} does not exist
     * @apiNote Part of “Use case 1” (as noted in code comments) though not fully implemented in UX yet.
     */
    @PostMapping
    public ResponseEntity<PlayerResponse> createPlayer(@RequestBody CreatePlayerRequest body) {
        Player created = playerService.createPlayer(
                body.name(), body.dateOfBirth(), body.country(),
                body.position(), body.goals(), body.teamId()
        );
        URI location = URI.create("/api/players/" + created.getId());
        return ResponseEntity.created(location).body(toResponse(created));
    }

    /**
     * Updates an existing player.
     * @param player player entity carrying the updated state
     * @return the updated {@link Player} entity
     */
    @PutMapping
    public Player updatePlayer(@RequestBody Player player) {
        return playerService.updatePlayer(player);
    }

    /**
     * Deletes a player by id.
     * @param id identifier of the player to remove
     */
    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
    }

    /**
     * Maps a {@link Player} entity to a {@link PlayerResponse} DTO.
     * @param p player entity (non-null)
     * @return mapped {@link PlayerResponse}
     */
    private PlayerResponse toResponse(Player p) {
        return new PlayerResponse(
                p.getId(),
                p.getName(),
                p.getPosition(),
                p.getGoals(),
                p.getCountry(),
                p.getDateOfBirth(),
                p.getTeam() != null ? p.getTeam().getId()   : null,
                p.getTeam() != null ? p.getTeam().getName() : null
        );
    }
}
