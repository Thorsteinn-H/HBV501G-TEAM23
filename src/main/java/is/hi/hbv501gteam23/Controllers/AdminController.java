package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.Entities.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that exposes read/write operations for {@link Team}, {@link Match} and {@link Player} resources.
 * Base path is /admin
 * Only endpoints are {push, patch, delete} for Admin role to perform.
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Get the current authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Add the username to the model to display in the view
        model.addAttribute("username", authentication.getName());

        // Return the admin dashboard view
        return "admin/dashboard";
    }
}
