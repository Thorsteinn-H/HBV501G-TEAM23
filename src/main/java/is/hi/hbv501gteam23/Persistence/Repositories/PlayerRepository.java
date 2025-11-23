package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing and managing {@link Player} entities.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {
    /**
     * Retrieves player from name
     * @param name the name of the player
     * @return A player matching the name
     */
    Player findByNameContainingIgnoreCase(String name);

    /**
     * This serves as a purpose when a team is deleted
     *      and the id is set to null on each player of the team
     * @param teamId the id of a team
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Player p set p.team = null where p.team.id = :teamId")
    void clearTeamByTeamId(@Param("teamId") Long teamId);
}
