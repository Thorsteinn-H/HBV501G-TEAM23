package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.Entities.Team;

import java.time.LocalDate;
import java.util.List;

public interface PlayerService {
    /**
     * Retrieves all players
     *
     * @return a list of all {@link Player} entities
     */
    List<Player> getAllPlayers();

    /**
     * Retrieves a single player by its unique identifier.
     *
     * @param id the ID of the match
     * @return the {@link Player} with the specified id
     */
    Player getPlayerById(Long id);


    /**
     * Retrieves a single player by its name
     *
     * @param name the name of the player
     * @return the {@link Player} with the specified name
     */
    Player searchPlayersByName(String name);

    /**
     * Retrieves a list of players by its team name
     *
     * @param team the ID of the match
     * @return a list of all {@link Player} entities with the specified team name
     */
    List<Player> getByTeamName(String team);

    /**
     * Retrieves a list of players by its team unique identifier.
     *
     * @param teamId the ID of the match
     * @return a list of all {@link Player} entities with the specified team id
     */
    List<Player> getByTeamId(Long teamId);

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
    Player createPlayer(String name, LocalDate dob, String country,
                        Player.PlayerPosition position, Integer goals, Long teamId);

    /**
     * Updates an existing player with new data
     *
     * @param player the {@link Player} entity with updated fields
     * @return the updated {@link Player} entity
     */
    Player updatePlayer(Player player);

    /**
     * Deletes a player by its id
     *
     * @param id the id of the player to delete
     */
    void deletePlayer(Long id);
}

