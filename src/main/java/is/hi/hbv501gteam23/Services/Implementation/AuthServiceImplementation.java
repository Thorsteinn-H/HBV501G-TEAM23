package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.Repositories.AuthRepository;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for handling user authentication and registration
 */
@Service
@Transactional
public class AuthServiceImplementation implements AuthService {
    private final AuthRepository authRepository;
    private final FavoriteService favoriteService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImplementation(AuthRepository authRepository, 
                                   FavoriteService favoriteService,
                                   PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.favoriteService = favoriteService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(String email, String password) {
        Optional<User> userOpt = authRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPasswordHash())) {
                // Ensure the user has a favorites entry
                ensureFavoritesExists(user.getId());
                return user;
            }
        }
        return null;
    }

    @Override
    public User register(User user) {
        // Check if user with this email already exists
        if (authRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        
        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);
        
        // Set default role if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("User");
        }
        
        // Set creation date
        user.setCreatedAt(LocalDate.now());
        
        // Save the user
        User savedUser = authRepository.save(user);
        
        // Create a favorites entry for the new user
        favoriteService.getOrCreateFavorites(savedUser.getId());
        
        return savedUser;
    }

    @Override
    public List<User> getAllActiveUsers() {
        return authRepository.findAllActiveUsers();
    }

    @Override
    public User findByEmail(String email) {
        return authRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User findById(Long id) {
        return authRepository.findById(id).orElse(null);
    }

    @Override
    public boolean validatePassword(String inputPassword, String storedPassword) {
        return passwordEncoder.matches(inputPassword, storedPassword);
    }
    
    @Override
    public void ensureFavoritesExists(Long userId) {
        favoriteService.getOrCreateFavorites(userId);

    @Override
    public void softDeleteUser(Long id) {
        User user = findById(id);
        if (user != null && user.isActive()) {
            user.setActive(false);
            user.setDeletedAt(LocalDateTime.now());
            user.setEmail("deleted_" + id + "@example.com");
            user.setName("Deleted User " + id);
            user.setPasswordHash(null);
            user.setGender(null);
            authRepository.save(user);
        }
    }
}
