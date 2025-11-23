package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto;
import java.util.List;

public interface PlayerService {

    /**
     * Finds players using optional filters, with sorting and pagination.
     * <p>
     * All filter parameters are optional; when {@code null} or blank, they are ignored.
     *
     * @param filter optional filter parameters
     * @return list of {@link Player} entities matching the given filters
     */
    List<Player> findPlayers(PlayerDto.PlayerFilter filter);

    /**
     * Retrieves a single player by its unique identifier.
     * @param id the ID of the match
     * @return the {@link Player} with the specified id
     */
    Player getPlayerById(Long id);

    /**
     * Creates a player
     * @param body the {@link Player} entity to create
     * @return the created player
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
     * Deletes a player by their id
     *
     * @param id the id of the player to delete
     */
    void deletePlayer(Long id);
}

