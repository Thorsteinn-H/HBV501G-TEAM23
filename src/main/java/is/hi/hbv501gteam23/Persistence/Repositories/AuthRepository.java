package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Nafn : Þorsteinn H. Erlendsson
 * Tölvupóstur: the85@hi.is
 * Lýsing: Repository for handling User authentication data
 */
public interface AuthRepository extends JpaRepository<User, Long> {
    // Find a user by email
    Optional<User> findByEmail(String email);
    
    // Login method - returns user if credentials are valid, null otherwise
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.passwordHash = :passwordHash")
    Optional<User> login(@Param("email") String email, @Param("passwordHash") String passwordHash);
    
    // Note: save() and delete() methods are already provided by JpaRepository
    // - User save(User user)
    // - void delete(User user)
}
