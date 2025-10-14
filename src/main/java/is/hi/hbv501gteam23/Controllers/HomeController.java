package is.hi.hbv501gteam23.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "redirect:/home";
    }
    
    @GetMapping("/home")
    public String showHome() {
        return "home";
    }
}
