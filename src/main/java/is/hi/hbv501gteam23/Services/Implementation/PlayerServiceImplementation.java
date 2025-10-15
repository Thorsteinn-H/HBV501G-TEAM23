package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.Repositories.PlayerRepository;
import is.hi.hbv501gteam23.Services.Interfaces.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PlayerServiceImplementation implements PlayerService {
    private final PlayerRepository playerRepository;

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Player> searchPlayersByName(String name) {
        return playerRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Player updatePlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }
}
