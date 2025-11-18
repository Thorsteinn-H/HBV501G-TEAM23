package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Favorite;
import is.hi.hbv501gteam23.Persistence.dto.FavoriteDto;

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
    FavoriteDto.FavoriteResponse addFavorite(Long userId, Favorite.EntityType type, Long entityId);

    /**
     * Removes an existing favorite for the given user and entity.
     *
     * @param userId   the ID of the user
     * @param type     the type of the entity
     * @param entityId the ID of the entity
     */
    void removeFavorite(Long userId, Favorite.EntityType type, Long entityId);

    /**
     * Checks whether a given entity is a favorite for the user.
     *
     * @param userId   the ID of the user
     * @param type     the type of the entity
     * @param entityId the ID of the entity
     * @return {@code true} if the entity is a favorite, otherwise {@code false}
     */
    boolean isFavorite(Long userId, Favorite.EntityType type, Long entityId);

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
     * @param type   the type of the favorited entities
     * @return list of favorites of the given type for the user
     */
    List<FavoriteDto.FavoriteResponse> listForUserAndType(Long userId, Favorite.EntityType type);
}
