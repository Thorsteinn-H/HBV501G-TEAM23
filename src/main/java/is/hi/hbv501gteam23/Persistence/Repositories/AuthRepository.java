package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
