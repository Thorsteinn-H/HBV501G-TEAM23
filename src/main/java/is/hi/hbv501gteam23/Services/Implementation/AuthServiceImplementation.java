package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.Repositories.AuthRepository;
import is.hi.hbv501gteam23.Persistence.dto.AuthDto;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Security.JwtTokenProvider;
import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import is.hi.hbv501gteam23.Services.Interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service implementation for handling authentication and registration
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {
    private final AuthRepository authRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Logs in a user with the given email and password.
     *
     * @param email     the email address of the user
     * @param password  the plain text password provided by the user
     * @return the {@link User} entity if credentials are valid,
     *
     */
    @Override
    public User login(String email, String password) {
        User user = userService.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    /**
     * Registers a new user with the provided details.
     *
     * @param request  a {@link UserDto.CreateUserRequest} containing the users information
     * @return the newly created {@link User} entity
     */
    @Override
    public User register(AuthDto.@Valid RegisterUserRequest request) {
        if (userService.findByEmail(request.email()) != null) {
            throw new RuntimeException("Email already in use");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setName(request.username());
        user.setRole("USER");
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        user.setGender(request.gender());
        return authRepository.save(user);
    }
}
