package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.dto.FavoriteDto;
import is.hi.hbv501gteam23.Persistence.enums.FavoriteType;
import java.util.List;

/**
 * Service interface for handling user favorites
 */
public interface FavoriteService {

    /**
     * Adds a new favorite for the given user and entity.
     *
     * @param userId   the ID of the user
     * @param type     the type of the entity (PLAYER, TEAM, MATCH)
     * @param entityId the ID of the entity to favorite
     * @return the created favorite as a {@link FavoriteDto.FavoriteResponse}
     */
    FavoriteDto.FavoriteResponse addFavorite(Long userId, FavoriteType type, Long entityId);

    /**
     * Removes an existing favorite for the given user and entity.
     *
     * @param userId   the ID of the user
     * @param type     the type of the entity
     * @param entityId the ID of the entity
     */
    void removeFavorite(Long userId, FavoriteType type, Long entityId);

    /**
     * Lists all favorites for a given user.
     *
     * @param userId the ID of the user
     * @return list of favorites for the user
     */
    List<FavoriteDto.FavoriteResponse> listAllForUser(Long userId);

    /**
     * Lists favorites for a given user filtered by entity type.
     *
     * @param userId the ID of the user
     * @param type   the type of the entities marked as favorite
     * @return list of favorites of the given type for the user
     */
    List<FavoriteDto.FavoriteResponse> listForUserAndType(Long userId, FavoriteType type);
}
