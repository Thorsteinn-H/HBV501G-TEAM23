package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.Entities.Team;

import java.time.LocalDate;
import java.util.List;

public interface PlayerService {
    List<Player> getAllPlayers();
    Player getPlayerById(Long id);
    Player searchPlayersByName(String name);
    List<Player> getByTeamName(String team);
    Player createPlayer(String name, LocalDate dob, String country,
                        Player.PlayerPosition position, Integer goals, Long teamId);
    Player updatePlayer(Player player);
    void deletePlayer(Long id);
}

