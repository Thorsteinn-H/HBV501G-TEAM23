package is.hi.hbv501gteam23.Persistence.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import is.hi.hbv501gteam23.Persistence.Entities.Favorite;
import is.hi.hbv501gteam23.Persistence.enums.FavoriteType;

/**
 * DTO types for creating and returning {@link Favorite} entities.
 */
public final class FavoriteDto {
    /**
     * Request body for creating a new favorite.
     *
     * @param favoriteType the type of entity to mark as favorite (PLAYER, TEAM, MATCH)
     * @param favoriteId   the ID of the entity to mark as favorite
     */
    public record CreateFavoriteRequest(
        @Schema(enumAsRef = true)
        FavoriteType favoriteType,

        Long favoriteId
    ) {}

    /**
     * Response DTO representing a favorite.
     *
     * @param id           the ID of the favorite
     * @param favoriteType the type of the entity
     * @param entityId     the ID of the entity
     */
    public record FavoriteResponse(
        Long id,
        FavoriteType favoriteType,
        Long entityId,
        String entityName
    ) {}
}
