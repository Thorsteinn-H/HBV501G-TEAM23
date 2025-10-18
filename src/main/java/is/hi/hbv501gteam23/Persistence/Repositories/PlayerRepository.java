package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByNameContainingIgnoreCase(String name);
    List<Player> findByTeam_Name(String teamName);
    List<Player> findByPosition(String position);
    List<Player> findByCountry(String country);
    List<Player> findByGoalsGreaterThan(int goals);
}
