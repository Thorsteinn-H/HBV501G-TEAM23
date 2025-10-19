package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.Repositories.PlayerRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.TeamRepository;
import is.hi.hbv501gteam23.Services.Interfaces.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for handling logic related to players
 */
@Service
@RequiredArgsConstructor
public class PlayerServiceImplementation implements PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    /**
     * Retrieves all players
     *
     * @return a list of all {@link Player} entities
     */
    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    /**
     * Retrieves a single player by its unique identifier.
     *
     * @param id the ID of the match
     * @return the {@link Player} with the specified id
     */
    @Override
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player "+id+" not found"));
    }

    /**
     * Retrieves a single player by its name
     *
     * @param name the name of the player
     * @return the {@link Player} with the specified name
     */
    @Override
    public Player searchPlayersByName(String name) {
        return playerRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Retrieves a list of players by its team name
     *
     * @param teamName the ID of the match
     * @return a list of all {@link Player} entities with the specified team name
     */
    @Override
    public List<Player> getByTeamName(String teamName) {
        return playerRepository.findByTeam_NameIgnoreCase(teamName);
    }

    /**
     * Retrieves a list of players by its team unique identifier.
     *
     * @param teamId the ID of the match
     * @return a list of all {@link Player} entities with the specified team id
     */
    @Override
    public List<Player> getByTeamId(Long teamId) {
        return playerRepository.findByTeamId(teamId);
    }


    /**
     * Creates a new player
     *
     * @param name     the name of the player
     * @param dob      the player's date of birth
     * @param country  the country the player represents
     * @param position the player's position
     * @param goals    the number of goals the player has scored
     * @param teamId   the id of the team to the player should be assigned
     * @return the newly created {@link Player} entity
     */
    @Override public Player createPlayer(String name, LocalDate dob, String country,
                                         Player.PlayerPosition position, Integer goals, Long teamId) {
        var team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team "+teamId+" not found"));
        var p = Player.builder()
                .name(name)
                .dateOfBirth(dob)
                .country(country)
                .position(position)
                .goals(goals != null ? goals : 0)
                .team(team)
                .build();
        return playerRepository.save(p);
    }

    /**
     * Updates an existing player with new data
     *
     * @param player the {@link Player} entity with updated fields
     * @return the updated {@link Player} entity
     */
    @Override
    public Player updatePlayer(Player player) {
        return playerRepository.save(player);
    }

    /**
     * Deletes a player by its id
     *
     * @param id the id of the player to delete
     */
    @Override
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }
}
