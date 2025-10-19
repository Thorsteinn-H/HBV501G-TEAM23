package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for accessing and managing {@link Player} entities.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    /**
     * Retrieves player from name
     * @param name the name of the player
     * @return A player matching the name
     */
    Player findByNameContainingIgnoreCase(String name);

    /**
     * Retrieves all players from the same team
     * @param name the name of the team
     * @return A list of players from the same team
     */
    List<Player> findByTeam_NameIgnoreCase(String name);

    /**
     * Retrieves all players from the same team
     * @param teamId a teams specific id
     * @return A list of players from the same team
     */
    List<Player> findByTeamId(Long teamId);

    /**
     * Retrieves all players with the same position
     * @param position a players position in football
     * @return A list of players with the same football position
     */
    List<Player> findByPosition(String position);

    /**
     *  Retrieves all players from the same country
     * @param country a players country of origin
     * @return A list of players from the same country
     */
    List<Player> findByCountry(String country);

    /**
     * Retrieves all players with equal or greater amount of goals
     * @param goals number of goals by a player
     * @return A list of players that have scored equal or greater amount of goals
     */
    List<Player> findByGoalsGreaterThan(int goals);
}
