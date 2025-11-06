package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto.PlayerResponse;
import is.hi.hbv501gteam23.Services.Interfaces.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    //UC7
    /**
     * Retrieves all players.
     * <p>
     *      This method retrieves all {@link Player} entities from the database
     * </p>
     * @return list of players mapped to {@link PlayerResponse}
     */
    @GetMapping
    public List<PlayerResponse> getAllPlayers() {
        return playerService.getAllPlayers()
                .stream().map(this::toResponse).toList();
    }

    //UC8
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
    public List<PlayerDto.PlayerResponse> getByVenueId(@PathVariable("teamId") Long teamId) {
        return playerService.getByTeamId(teamId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/isActive={isActive}")
    public List<PlayerDto.PlayerResponse> getActivePlayers(@PathVariable("isActive") Boolean isActive) {
        return playerService.getActivePlayers(isActive)
                .stream()
                .map(this::toResponse)
                .toList();
    }

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
