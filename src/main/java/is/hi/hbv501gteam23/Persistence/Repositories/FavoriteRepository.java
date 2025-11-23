package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.Favorite;
import is.hi.hbv501gteam23.Persistence.enums.FavoriteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserIdAndEntityTypeAndEntityId(Long userId, FavoriteType type, Long entityId);
    List<Favorite> findByUserId(Long userId);
    List<Favorite> findByUserIdAndEntityType(Long userId, FavoriteType type);
}
