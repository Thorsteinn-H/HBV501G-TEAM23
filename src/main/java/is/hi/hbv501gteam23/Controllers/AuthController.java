package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login user", description = "Authenticate a user and create a session")
    @ApiResponse(responseCode = "200", description = "User logged in successfully")
    @PostMapping("/login")
    public String login(@RequestParam String email,
                       @RequestParam String password,
                       HttpSession session,
                       Model model) {
        try {
            // This method is now just a fallback, as Spring Security will handle the actual authentication
            return "redirect:/api/auth/loggedin";
        } catch (Exception e) {
            return "redirect:/api/auth/login?error";
        }
    }

    @GetMapping("/loggedin")
    public String loggedIn(HttpSession session, Model model) {
        // Get the authenticated user's username from the security context
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Get the user from the database
        User user = authService.findByEmail(username);
        if (user == null) {
            return "redirect:/api/auth/login?error=user_not_found";
        }

        // Store user info in session
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getName());

        model.addAttribute("user", user);
        return "LoggedInUser";
    }

    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
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
        session.setAttribute("userId", savedUser.getId());
        session.setAttribute("userName", savedUser.getName());

        return "redirect:/api/auth/loggedin";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            switch (error) {
                case "missing_fields":
                    model.addAttribute("error", "Email, username, and password are required");
                    break;
                case "email_exists":
                    model.addAttribute("error", "Email already in use");
                    break;
                case "registration_failed":
                    model.addAttribute("error", "Registration failed. Please try again.");
                    break;
            }
        }
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/?logout";
    }

    /**
     *
     * @return
     */
    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "List of all users")
    @GetMapping("/users")
    @ResponseBody
    public List<User> getAllUsers() {
        return authService.getAllUsers();
    }

    /**
     *
     * @param id
     * @return
     */
    @Operation(summary = "Get a specific user")
    @ApiResponse(responseCode = "200", description = "User found")
    @GetMapping("/user/{id}")
    @ResponseBody
    public User getUser(@PathVariable Long id) {
        return authService.findById(id);
    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
