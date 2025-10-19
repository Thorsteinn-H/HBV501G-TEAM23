package is.hi.hbv501gteam23.Controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard(Model model) {
        // Get the current authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Add the username to the model to display in the view
        model.addAttribute("username", authentication.getName());
        
        // Return the admin dashboard view
        return "admin/dashboard";
    }
}
