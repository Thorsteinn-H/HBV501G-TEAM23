package is.hi.hbv501gteam23.Persistence.dto;

import is.hi.hbv501gteam23.Persistence.Entities.Favorite;

public final class FavoriteDto {

    public record createFavoriteRequest(
            Favorite.EntityType entityType,
            Long entityId
    ) {}

    public record favoriteResponse(
            Long id,
            Long userId,
            Favorite.EntityType entityType,
            Long entityId
    ) {}
}
