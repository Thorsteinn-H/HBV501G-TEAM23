package is.hi.hbv501gteam23.Persistence.dto;

import is.hi.hbv501gteam23.Persistence.Entities.Favorite;

/**
 * DTO types for creating and returning {@link Favorite} entities.
 */
public final class FavoriteDto {
    /**
     * Request body for creating a new favorite.
     *
     * @param entityType the type of entity to mark as favorite (PLAYER, TEAM, MATCH)
     * @param entityId   the ID of the entity to mark as favorite
     */
    public record CreateFavoriteRequest(
            Favorite.EntityType entityType,
            Long entityId
    ) {}

    /**
     * Response DTO representing a favorite.
     *
     * @param id         the ID of the favorite
     * @param userId     the ID of the user that owns the favorite
     * @param entityType the type of the favorited entity
     * @param entityId   the ID of the favorited entity
     */
    public record FavoriteResponse(
            Long id,
            Long userId,
            Favorite.EntityType entityType,
            Long entityId
    ) {}
}
