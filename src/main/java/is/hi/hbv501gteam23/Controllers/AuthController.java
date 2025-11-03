package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.LoginDto;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Security.CustomUserDetails;
import is.hi.hbv501gteam23.Security.JwtTokenProvider;
import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate and return JWT token")
    public ResponseEntity<LoginDto.LoginResponse> login(@Valid @RequestBody LoginDto.LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication.getName());

        User user = authService.findByEmail(authentication.getName());
        return ResponseEntity.ok(new LoginDto.LoginResponse(token, toResponse(user)));
    }

    @GetMapping("/loggedin")
    @Operation(summary = "Get currently logged in user")
    public ResponseEntity<UserDto.UserResponse> loggedIn(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = authService.findByEmail(userDetails.getUsername());
        if (user == null || !user.isActive()) throw new EntityNotFoundException("User not found");
        return ResponseEntity.ok(toResponse(user));
    }

    /**
     *
     * @param request
     * @return
     */
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Creates a new user account")
    @ApiResponse(responseCode = "201", description = "User created")
    public ResponseEntity<UserDto.UserResponse> registerUser(@Valid @RequestBody UserDto.CreateUserRequest request) {
        User created = authService.registerUser(request);
        return ResponseEntity.ok(toResponse(created));
    }

    /**
     *
     * @return
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Returns all non-deleted users")
    @ApiResponse(responseCode = "200", description = "List of all users")
    public List<UserDto.UserResponse> getAllUsers() {
        return authService.getAllActiveUsers().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "200", description = "User found")
    public ResponseEntity<UserDto.UserResponse> getUser(@PathVariable Long id) {
        User user = authService.findById(id);
        if (user == null || !user.isActive()) {
            throw new EntityNotFoundException("User not found");
        }
        return ResponseEntity.ok(toResponse(user));
    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user", description = "Marks user as inactive and anonymizes user data")
    @ApiResponse(responseCode = "200", description = "User successfully deleted")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        authService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private UserDto.UserResponse toResponse(User u) {
        return new UserDto.UserResponse(
                u.getId(),
                u.getEmail(),
                u.getName(),
                u.getGender(),
                u.getRole(),
                u.isActive(),
                u.getCreatedAt()
        );
    }
}

