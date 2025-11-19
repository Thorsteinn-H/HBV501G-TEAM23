package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository for accessing and managing {@link Match} entities.
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> , JpaSpecificationExecutor<Match> {
    /**
     * Retrieves all matches where either the home team or the away team
     * matches one of the given team IDs.
     *
     * @param teamId1 the ID of the first team
     * @param teamId2 the ID of the second team
     * @return list of matches where {@code teamId1} is the home or away team,
     * or {@code teamId2} is the home or away team
     */
    List<Match> findByHomeTeam_IdOrAwayTeam_Id(Long teamId1, Long teamId2);

    /**
     * Retrieves all matches played within the given date range,
     * ordered by date in ascending order.
     *
     * @param from the start date
     * @param to   the end date
     * @return list of matches whose {@code date} is between {@code from} and {@code to},
     * ordered from oldest to newest
     */
    List<Match> findByMatchDateBetweenOrderByMatchDateAsc(LocalDate from, LocalDate to);
}
