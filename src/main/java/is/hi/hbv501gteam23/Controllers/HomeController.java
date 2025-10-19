package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    
    private final AuthService authService;
    
    @Autowired
    public HomeController(AuthService authService) {
        this.authService = authService;
    }
    
    @GetMapping("/")
    public String home(@RequestParam(required = false) String logout, Model model) {
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "redirect:/home";
    }
    
    @GetMapping("/home")
    public String showHome(Model model) {
        // Check if user is logged in
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            User user = authService.findByEmail(auth.getName());
            if (user != null) {
                model.addAttribute("currentUser", user);
            }
        }
        return "home";
    }
}
