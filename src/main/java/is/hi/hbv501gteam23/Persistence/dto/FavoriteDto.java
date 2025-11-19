package is.hi.hbv501gteam23.Persistence.dto;

import is.hi.hbv501gteam23.Persistence.enums.FavoriteType;

public final class FavoriteDto {

    public record CreateFavoriteRequest(
        FavoriteType favoriteType,
        Long favoriteId
    ) {}

    public record FavoriteResponse(
        Long id,
        Long userId,
        FavoriteType favoriteType,
        Long favoriteId
    ) {}
}
