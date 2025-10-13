package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Nafn : Þorsteinn H. Erlendsson
 * Tölvupóstur: the85@hi.is
 * Lýsing:
 **/

@RestController
@RequestMapping("/user")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService AuthService){
        this.authService = AuthService;
    }


    @GetMapping
    public Boolean login(@PathVariable String username, @PathVariable String password){

        return authService.login(username,password);
    }

    @PostMapping
    public User register(@PathVariable String username, @PathVariable String password){
        return authService.register(username,password);
    }

    @PostMapping
    public void logout(){

    }

    @DeleteMapping("/user/{id}")
    public void remove(@PathVariable Long id){

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth")
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (authService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        User newUser = authService.register(user);
        return ResponseEntity.ok(newUser);
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
