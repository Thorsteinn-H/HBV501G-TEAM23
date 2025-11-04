package is.hi.hbv501gteam23.Services.Interfaces;


import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto;
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
     * Retrieves a list of active players
     *
     * @param isActive the active status of the player
     * @return a list of all {@link Player} entities with active status
     */
    List<Player> getActivePlayers(Boolean isActive);

    /**
     *
     * @param body
     * @return
     */
    Player createPlayer(PlayerDto.CreatePlayerRequest body);

    /**
     * Updates existing player
     * @param id the id of the player to update
     * @param body partial update payload for the player
     * @return 200 OK with the updated {@link PlayerDto.PlayerResponse};
     */
    Player patchPlayer(Long id, PlayerDto.PatchPlayerRequest body);

    /**
     * Deletes a player by its id
     *
     * @param id the id of the player to delete
     */
    void deletePlayer(Long id);
}

