package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {
    // Find a user by email
    Optional<User> findByEmail(String email);

    // Find active users only (not deleted)
    @Query("SELECT u FROM User u WHERE u.isActive = true")
    List<User> findAllActiveUsers();

    // Login method - returns user if credentials are valid, null otherwise
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.passwordHash = :passwordHash")
    Optional<User> login(@Param("email") String email, @Param("passwordHash") String passwordHash);
}
