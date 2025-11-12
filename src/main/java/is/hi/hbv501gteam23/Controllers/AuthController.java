package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.AuthDto;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Security.JwtTokenProvider;
import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import is.hi.hbv501gteam23.Services.Interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that exposes authentication and registration endpoints.
 * Base path is /auth
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Logs in a user with the given email and password.
     * @param req a {@link AuthDto.LoginResponse} containing the users log in information
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates user and returns JWT token.")
    public ResponseEntity<AuthDto.LoginResponse> login(@Valid @RequestBody AuthDto.LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication.getName());

        User user = userService.findByEmail(authentication.getName());
        return ResponseEntity.ok(new AuthDto.LoginResponse(token, toResponse(user)));
    }

    /**
     * Registers a new user with the provided details.
     *
     * @param request  a {@link UserDto.CreateUserRequest} containing the users information
     * @return a {@link UserDto.UserResponse} containing the mapped data
     */
    @PostMapping("/signup")
    @Operation(summary = "Sign up user", description = "Creates a new user account.")
    public ResponseEntity<AuthDto.LoginResponse> register(@Valid @RequestBody AuthDto.RegisterUserRequest request) {
        User newUser = authService.register(request);
        String token = jwtTokenProvider.generateToken(newUser.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthDto.LoginResponse(token, toResponse(newUser)));
    }

    /**
     * Maps a {@link User} entity to a {@link UserDto.UserResponse} DTO.
     * @param user the {@link User} entity to map
     * @return a {@link UserDto.UserResponse} containing mapped data
     */
    private UserDto.UserResponse toResponse(User user) {
        return new UserDto.UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getGender(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt(),
                user.getImage(),
                user.getImageType()
        );
    }
}
