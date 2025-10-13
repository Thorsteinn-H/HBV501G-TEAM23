package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
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

    }
}
