package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Favorites;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
import is.hi.hbv501gteam23.Services.Interfaces.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavouriteController {
    //private FavoriteService favoriteService;

    /*
    @GetMapping
    public List<Favorites> getAllFavorites(){
        return favoriteService.listAll();
    }
     */

    /*
    @PostMapping("/{id}")
    public Favorites addPlayerFavorite(@PathVariable Long id){
        return favoriteService.addPlayerFavorite(id);
    }
     */

    /*
    @PostMapping("/{id}")
    public Favorites addMatchToFavorites(@PathVariable Long id){
        return favoriteService.addMatchFavorite(id);
    }
     */

    /*
    @DeleteMapping("/{id}")
    public void removeFavorite(@PathVariable Long id){
        favoriteService.removeFavorite(id);
    }
     */
}
