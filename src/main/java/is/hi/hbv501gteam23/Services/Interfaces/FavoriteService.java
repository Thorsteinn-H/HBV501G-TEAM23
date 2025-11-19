package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.dto.FavoriteDto;
import is.hi.hbv501gteam23.Persistence.enums.FavoriteType;

import java.util.List;

/**
 * Service interface for handling user favorites
 */
public interface FavoriteService {
    FavoriteDto.FavoriteResponse addFavorite(Long userId, FavoriteType type, Long entityId);
    void removeFavorite(Long userId, FavoriteType type, Long entityId);
    boolean isFavorite(Long userId, FavoriteType type, Long entityId);

    List<FavoriteDto.FavoriteResponse> listAllForUser(Long userId);
    List<FavoriteDto.FavoriteResponse> listForUserAndType(Long userId, FavoriteType type);
}
