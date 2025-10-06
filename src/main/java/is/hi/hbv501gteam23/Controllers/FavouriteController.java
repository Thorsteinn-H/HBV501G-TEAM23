package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Favorites;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
import is.hi.hbv501gteam23.Services.Interfaces.PlayerService;
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
public class FavouriteController {
    private FavoriteService favoriteService;

    @Autowired
    public FavouriteController(FavoriteService FavoriteService){
        this.favoriteService = FavoriteService;
    }

    public static List<Favorites> getAllFavorites(){
        return null;
    }

    public static Favorites addPlayerFavorite(Long id){
        return null;
    }

    public static Favorites addMatchToFavorites(Long id){
        return null;
    }

    public static void removeFavorite(Long id){

    }

}
