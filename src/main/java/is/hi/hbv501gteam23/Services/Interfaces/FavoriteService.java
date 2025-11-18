package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Favorite;
import is.hi.hbv501gteam23.Persistence.dto.FavoriteDto;

import java.util.List;

/**
 * Service interface for handling user favorites
 */
public interface FavoriteService {
    FavoriteDto.FavoriteResponse addFavorite(Long userId, Favorite.EntityType type, Long entityId);
    void removeFavorite(Long userId, Favorite.EntityType type, Long entityId);
    boolean isFavorite(Long userId, Favorite.EntityType type, Long entityId);

    List<FavoriteDto.FavoriteResponse> listAllForUser(Long userId);
    List<FavoriteDto.FavoriteResponse> listForUserAndType(Long userId, Favorite.EntityType type);
}
