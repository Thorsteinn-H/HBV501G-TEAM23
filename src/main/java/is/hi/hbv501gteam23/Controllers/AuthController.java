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
    public String login(@RequestParam String email, 
                       @RequestParam String password, 
                       HttpSession session,
                       Model model) {
        try {
            User user = authService.login(email, password);
            if (user != null) {
                // Store user info in session
                session.setAttribute("userId", user.getId());
                session.setAttribute("userName", user.getName());
                return "redirect:/api/auth/loggedin";
            }
            return "redirect:/api/auth/login?error=invalid_credentials";
        } catch (Exception e) {
            return "redirect:/api/auth/login?error=" + e.getMessage();
        }
    }

    @GetMapping("/loggedin")
    public String loggedIn(HttpSession session, Model model) {
        // Get the authenticated user's username from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/api/auth/login?error=not_authenticated";
        }
        
        String username = authentication.getName();
        
        // Get the user from the database
        User user = authService.findByEmail(username);
        if (user == null) {
            return "redirect:/api/auth/login?error=user_not_found";
        }
        
        // Ensure favorites entry exists
        authService.ensureFavoritesExists(user.getId());
        
        // Store user info in session
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getName());
        
        model.addAttribute("user", user);
        return "LoggedInUser";
    }

    /**
     *
     * @param request
     * @return
     */
    @PostMapping("/register")
    public String register(@RequestParam String email, 
                         @RequestParam("user_name") String name,
                         @RequestParam String password,
                         @RequestParam(required = false) String gender,
                         HttpSession session) {
        
        if (email == null || name == null || password == null) {
            return "redirect:/api/auth/register?error=missing_fields";
        }
        
        // Check if email already exists
        if (authService.findByEmail(email) != null) {
            return "redirect:/api/auth/register?error=email_exists";
        }
        
        // Create new user
        User newUser = User.builder()
            .email(email)
            .name(name)
            .passwordHash(password) // Will be hashed in the service
            .gender(gender)
            .role("User")
            .createdAt(LocalDate.now())
            .build();
        
        // Save user and log them in
        User savedUser = authService.register(newUser);
        
        // Ensure favorites entry exists
        authService.ensureFavoritesExists(savedUser.getId());
        
        session.setAttribute("userId", savedUser.getId());
        session.setAttribute("userName", savedUser.getName());
        
        return "redirect:/api/auth/loggedin";
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
