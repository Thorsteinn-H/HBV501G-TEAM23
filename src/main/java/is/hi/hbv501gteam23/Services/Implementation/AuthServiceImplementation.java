package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.Repositories.AuthRepository;
import is.hi.hbv501gteam23.Persistence.dto.AuthDto;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Persistence.enums.SystemRole;
import is.hi.hbv501gteam23.Security.CustomUserDetails;
import is.hi.hbv501gteam23.Security.JwtTokenProvider;
import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import is.hi.hbv501gteam23.Services.Interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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
     * @param email    the email address of the user
     * @param password the plain text password provided by the user
     * @return the {@link User} entity if credentials are valid,
     *
     */
    @Override
    public AuthDto.AuthResponse login(String email, String password) {
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return new AuthDto.AuthResponse(accessToken, refreshToken, mapToDto(user));
    }

    /**
     * Registers a new user with the provided details.
     *
     * @param request a {@link UserDto.CreateUserRequest} containing the users information
     * @return the newly created {@link User} entity
     */
    @Override
    public AuthDto.AuthResponse register(AuthDto.RegisterUserRequest request) {
        if (userService.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setName(request.username());
        user.setGender(request.gender());
        user.setRole(SystemRole.USER);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);

        User savedUser = authRepository.save(user);

        CustomUserDetails userDetails = new CustomUserDetails(savedUser);
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser);

        return new AuthDto.AuthResponse(accessToken, refreshToken, mapToDto(savedUser));
    }

    /**
     * Maps a {@link User} entity to a {@link UserDto.UserResponse} DTO.
     *
     * @param user the user entity to map
     * @return the mapped {@link UserDto.UserResponse}
     */
    private UserDto.UserResponse mapToDto(User user) {
        return new UserDto.UserResponse(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getGender(),
            user.getRole(),
            user.isActive(),
            user.getCreatedAt(),
            user.getProfileImage()
        );
    }
}
