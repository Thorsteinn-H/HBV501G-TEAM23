package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.User;

/**
 * Nafn : Þorsteinn H. Erlendsson
 * Tölvupóstur: the85@hi.is
 * Lýsing: Service interface for handling user authentication operations
 */
public interface AuthService {
    User login(String email, String password);
    User register(User user);
    User findByEmail(String email);
    User findById(Long id);
    boolean validatePassword(String rawPassword, String hashedPassword);
    
    /**
     * Ensures that a favorites entry exists for the given user
     * @param userId The ID of the user
     */
    void ensureFavoritesExists(Long userId);
}
