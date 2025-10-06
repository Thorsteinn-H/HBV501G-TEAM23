package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
/**
 * Nafn : Þorsteinn H. Erlendsson
 * Tölvupóstur: the85@hi.is
 * Lýsing:
 **/

@Controller
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService AuthService){
        this.authService = AuthService;
    }


    public static Boolean login(String username, String password){
        return Boolean.TRUE;
    }

    public static User register(String username, String password){
        return null;
    }

    public static void logout(){

    }

    public static void remove(User user){

    }
}
