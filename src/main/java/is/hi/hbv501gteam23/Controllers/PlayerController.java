package is.hi.hbv501gteam23.Controllers;


import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Services.Interfaces.MatchService;
import is.hi.hbv501gteam23.Services.Interfaces.PlayerService;
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
@RequestMapping("/player")
public class PlayerController {
    private PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService PlayerService){
        this.playerService = PlayerService;
    }

    @GetMapping
    public  List<Player> getAllPlayers() {
        return playerService.listAll();
    }

    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        return playerService.getById(id);
    }

    @GetMapping("/{name}")
    public List<Player> getPlayerByName(@PathVariable String name) {
        return playerService.searchByName(name);
    }

    @PostMapping
    public Player createPlayer(Player player){
        return playerService.create(player);
    }

    @PutMapping("/{player}")
    public Player updatePlayer(Player player){
        return playerService.update(player);
    }

    @DeleteMapping("/{player}")
    public void deletePlayer(@PathVariable Long player){
        playerService.delete(player);

    }



}
