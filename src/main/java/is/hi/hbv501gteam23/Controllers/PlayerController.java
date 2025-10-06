package is.hi.hbv501gteam23.Controllers;


import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Services.Interfaces.MatchService;
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
public class PlayerController {
    private PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService PlayerService){
        this.playerService = PlayerService;
    }

    public static List<Player> getAllPlayers() {
        return null;
    }

    public static Player getPlayerById(Long id) {
        return null;
    }

    public static List<Player> getPlayerByName(String name) {
        return null;
    }

    public static Player createPlayer(Player player){
        return null;
    }

    public static Player updatePlayer(Player player){
        return null;
    }

    public static void deletePlayer(Player player){

    }



}
