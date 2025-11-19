package is.hi.hbv501gteam23.Services.Interfaces;


import is.hi.hbv501gteam23.Persistence.Entities.Country;
import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto;

import java.util.List;

public interface PlayerService {

    /**
     * Finds players using optional filters, with sorting and pagination.
     * <p>
     * All filter parameters are optional; when {@code null} or blank, they are ignored.
     *
     * @param name     player name filter
     * @param teamId   team ID to filter by
     * @param teamName team name filter
     * @param country  country code to filter by
     * @param isActive active status filter
     * @param sortBy   field to sort by
     * @param sortDir  sort direction, either {@code "ASC"} or {@code "DESC"}
     * @param page     zero-based page index
     * @param size     page size (maximum number of players per page)
     * @return list of {@link Player} entities matching the given filters
     */
    List<Player> findPlayers(
        String name,
        Long teamId,
        String teamName,
        String country,
        Boolean isActive,
        String sortBy,
        String sortDir,
        int page,
        int size
    );

    /**
     * Retrieves all players
     * @return a list of all {@link Player} entities
     */
    List<Player> getAllPlayers();

    /**
     * Retrieves a single player by its unique identifier.
     * @param id the ID of the match
     * @return the {@link Player} with the specified id
     */
    Player getPlayerById(Long id);

    /**
     * Retrieves a single player by its name
     * @param name the name of the player
     * @return the {@link Player} with the specified name
     */
    Player searchPlayersByName(String name);

    /**
     * Retrieves a list of players by its team name
     * @param team the ID of the match
     * @return a list of all {@link Player} entities with the specified team name
     */
    List<Player> getByTeamName(String team);

    /**
     * Retrieves a list of players by its team unique identifier.
     * @param teamId the ID of the match
     * @return a list of all {@link Player} entities with the specified team id
     */
    List<Player> getByTeamId(Long teamId);

    /**
     * Retrieves a list of active players
     * @param isActive the active status of the player
     * @return a list of all {@link Player} entities with active status
     */
    List<Player> getActivePlayers(Boolean isActive);

    /**
     * Retrieves a list of players from a specific country
     * @param country the country to filter by
     * @return a list of all {@link Player} entities from the specified country
     */
    List<Player> findByPlayerCountry(Country country);

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
     * Deletes a player by its id
     *
     * @param id the id of the player to delete
     */
    void deletePlayer(Long id);
}

