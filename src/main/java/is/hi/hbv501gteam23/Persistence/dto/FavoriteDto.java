package is.hi.hbv501gteam23.Persistence.dto;

import is.hi.hbv501gteam23.Persistence.Entities.Favorite;

public final class FavoriteDto {

    public record CreateFavoriteRequest(
            Favorite.EntityType entityType,
            Long entityId
    ) {}

    public record FavoriteResponse(
            Long id,
            Long userId,
            Favorite.EntityType entityType,
            Long entityId
    ) {}
}
