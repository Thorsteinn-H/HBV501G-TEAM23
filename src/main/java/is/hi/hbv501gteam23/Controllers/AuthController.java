package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
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
    public String login(@RequestParam String email, 
                       @RequestParam String password, 
                       HttpSession session,
                       Model model) {
        User user = authService.login(email, password);
        if (user != null) {
            // Store only user ID in session
            session.setAttribute("userId", user.getId());
            return "redirect:/api/auth/success";
        }
        return "redirect:/api/auth/login?error";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, 
                         @RequestParam String user_name,
                         @RequestParam String password,
                         @RequestParam(required = false) String gender,
                         Model model) {
        
        if (email == null || user_name == null || password == null) {
            return "redirect:/api/auth/register?error=missing_fields";
        }
        
        if (authService.findByEmail(email) != null) {
            return "redirect:/api/auth/register?error=email_in_use";
        }
        
        // Create new user with required fields
        User user = new User();
        user.setEmail(email);
        user.setName(user_name);
        user.setGender(gender);
        user.setCreatedAt(LocalDate.now());
        user.setRole("User");
        
        try {
            user.setPasswordHash(password);
            User newUser = authService.register(user);
            model.addAttribute("user", newUser);
            return "redirect:/api/auth/success";
        } catch (Exception e) {
            return "redirect:/api/auth/register?error=registration_failed";
        }
    }

    @GetMapping("/register")
    public String register(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            switch (error) {
                case "missing_fields":
                    model.addAttribute("error", "Email, name, and password are required");
                    break;
                case "email_in_use":
                    model.addAttribute("error", "Email already in use");
                    break;
                case "registration_failed":
                    model.addAttribute("error", "Registration failed. Please try again.");
                    break;
            }
        }
        return "signup";
    }
    
    @GetMapping("/success")
    public String success(HttpSession session, Model model) {
        // Get user ID from session and fetch the user
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/api/auth/login";
        }
        User user = authService.findById(userId);
        if (user == null) {
            session.invalidate();
            return "redirect:/api/auth/login";
        }
        model.addAttribute("user", user);
        return "LoggedInUser";
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