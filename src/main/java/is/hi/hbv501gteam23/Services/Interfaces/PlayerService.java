package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Player;

import java.util.List;

public interface PlayerService {
    List<Player> getAllPlayers();
    Player getPlayerById(Long id);
    List<Player> searchPlayersByName(String name);
    Player createPlayer(Player player);
    Player updatePlayer(Player player);
    void deletePlayer(Long id);
}

