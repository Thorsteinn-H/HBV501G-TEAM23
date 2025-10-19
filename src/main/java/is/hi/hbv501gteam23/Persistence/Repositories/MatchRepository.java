package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository for accessing and managing {@link Match} entities.
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    /**
     * Retrieves all matches by home team or away team
     * @param teamId1 id of the first team
     * @param teamId2 id of the second team
     * @return List of matches where either teamId1 or teamId2 were present
     */
    List<Match> findByHomeTeam_IdOrAwayTeam_Id(Long teamId1, Long teamId2);

    /**
     * Retrieves all matches between start date and end start
     * @param start the start of when a match can happen
     * @param end the end of when a match can happen
     * @return List of all matches that happened between star date and end date
     */
    List<Match> findByDateBetween(LocalDate start, LocalDate end);
}
