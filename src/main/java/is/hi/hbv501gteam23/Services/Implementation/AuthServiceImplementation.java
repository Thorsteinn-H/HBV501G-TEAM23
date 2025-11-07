package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.Repositories.AuthRepository;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class AuthServiceImplementation implements AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImplementation(AuthRepository authRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(String email, String password) {
        // This method is kept for backward compatibility
        // Actual authentication is now handled by Spring Security
        User user = findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    @Override
    public User register(User user) {
        // Check if user with this email already exists
        if (findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already in use");
        }
        
        // Set default role if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("User");
        }
        
        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);
        
        return authRepository.save(user);
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
    public User updatePassword(User user, UserDto.updatePassword request) {

        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Old password does not match" );

        } else {

            String hashedNewPassword = passwordEncoder.encode(request.newPassword());
            user.setPasswordHash(hashedNewPassword);
            return authRepository.save(user);
        }

    }

    @Override
    public User updateGender(User user,  UserDto.updateGender request) {

        user.setGender(request.newGender());
        return authRepository.save(user);

    }

    @Override
    public User updateUsername(User user, UserDto.updateUsername request) {

        user.setName(request.newUsername());
        return authRepository.save(user);
    }

}
