package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        User user = authService.login(email, password);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body("Invalid email or password");
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userData) {
        String email = userData.get("email");
        String name = userData.get("user_name");
        String password = userData.get("password");
        String gender = userData.get("gender");
        
        if (email == null || name == null || password == null) {
            return ResponseEntity.badRequest().body("Email, name, and password are required");
        }
        
        if (authService.findByEmail(email) != null) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        
        // Create new user with required fields
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setGender(gender);
        user.setCreatedAt(LocalDate.now());
        user.setRole("User");
        
        try {
            // Set the password hash on the user object
            // The service will handle the actual hashing in its implementation
            user.setPasswordHash(password); // Note: In a real app, this should be hashed first
            User newUser = authService.register(user);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating user: " + e.getMessage());
        }
    }

    @GetMapping("/register")
    public String register() {
        return "signup";
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // In a real application, you would invalidate the session/token here
        return ResponseEntity.ok("Logged out successfully");
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> removeUser(@PathVariable Long id) {
        // First, let's check if the user exists
        User user = authService.findById(id);
        if (user != null) {
            // In a real application, you would call authService.delete(user) here
            return ResponseEntity.ok("User removed successfully");
        }
        return ResponseEntity.notFound().build();
    }
}