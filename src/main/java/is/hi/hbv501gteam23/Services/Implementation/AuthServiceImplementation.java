package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.Repositories.AuthRepository;
import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImplementation implements AuthService {
    private final AuthRepository authRepository;

    @Autowired
    public AuthServiceImplementation(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public User login(String email, String password) {
        User user = findByEmail(email);
        if (user != null && validatePassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    @Override
    public User register(User user) {
        // Store password as plain text (temporarily)
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
        // Simple string comparison (temporary)
        return inputPassword != null && inputPassword.equals(storedPassword);
    }
}
