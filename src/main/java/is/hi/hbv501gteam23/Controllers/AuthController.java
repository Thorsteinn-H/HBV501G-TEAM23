package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.dto.AuthDto;
import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication and registration endpoints.
 * Provides endpoints for logging in and signing up users.
 * Base URL for this controller is `/auth`.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticates a user with the provided email and password.
     * Returns a JWT access token and refresh token if successful.
     *
     * @param req a {@link AuthDto.AuthResponse} containing the email and password
     * @return a {@link ResponseEntity} containing an {@link AuthDto.AuthResponse}
     *      with the access token, refresh token, and user information
     */
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates user and returns JWT access- and refresh tokens")
    public ResponseEntity<AuthDto.AuthResponse> login(@Valid @RequestBody AuthDto.LoginUserRequest req) {
        AuthDto.AuthResponse response = authService.login(req.email(), req.password());
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new user account and returns a JWT token so
     * the user is automatically authenticated after registration.
     *
     * @param req a {@link AuthDto.RegisterUserRequest} containing the user's
     *             email, username, password, and optional gender
     * @return a {@link ResponseEntity} containing an {@link AuthDto.AuthResponse}
     *             with the access token, refresh token, and user information
     */
    @PostMapping("/signup")
    @Operation(summary = "Sign up user", description = "Creates a new user account with the USER role")
    public ResponseEntity<AuthDto.AuthResponse> register(@Valid @RequestBody AuthDto.RegisterUserRequest req) {
        AuthDto.AuthResponse response = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
