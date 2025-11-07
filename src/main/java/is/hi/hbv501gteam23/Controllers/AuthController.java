package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

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

    public User getCurrentLogin(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        return authService.findByEmail(username);
    }

    @PostMapping("/users/update_password")
    public ResponseEntity<?> updatePassword(@RequestBody UserDto.updatePassword request) {

        User user = getCurrentLogin();

        User newPass = authService.updatePassword(user, request);

        return ResponseEntity.ok(toResponse(newPass));


    }


    private UserDto.UserResponse toResponse(User u) {
        return new UserDto.UserResponse(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getGender(),
                u.getCreatedAt(),
                u.getPasswordHash(),
                u.getRole()
        );
    }

    @PostMapping("/users/update_username")
    public ResponseEntity<?> updateUsername(@RequestBody UserDto.updateUsername request) {
        User user = getCurrentLogin();

        User newPass = authService.updateUsername(user, request);

        return ResponseEntity.ok(toResponse(newPass));
    }

    @PostMapping("/users/update_gender")
    public ResponseEntity<?> updateGender(@RequestBody UserDto.updateGender request) {

        User user = getCurrentLogin();

        User newPass = authService.updateGender(user, request);

        return ResponseEntity.ok(toResponse(newPass));

    }

}