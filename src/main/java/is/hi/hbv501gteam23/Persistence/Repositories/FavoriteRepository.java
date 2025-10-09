package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorites, Long> {
    // GET user's favorites
    List<Favorites> findByUserId(Long userId);

    // Check if player/match has already been favorited
    boolean existsByUserIdAndPlayerId(Long userId, Long playerId);
    boolean existsByUserIdAndMatchId(Long userId, Long matchId);

    // Remove a player/match from favorites
    void deleteByUserIdAndPlayerId(Long userId, Long playerId);
    void deleteByUserIdAndMatchId(Long userId, Long matchId);
}
