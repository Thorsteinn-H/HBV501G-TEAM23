package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorites, Long> {
    /**
     * Find favorites by user ID
     * @param userId The ID of the user
     * @return Optional containing the favorites if found
     */
    Optional<Favorites> findById(Long userId);
}
