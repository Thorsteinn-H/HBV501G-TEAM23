package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByHomeTeamName(String teamName);
    List<Match> findByAwayTeamName(String teamName);
    List<Match> findByHomeTeamNameAndAwayTeamName(String homeTeam, String awayTeam);
    List<Match> findByDateBetween(LocalDate start,LocalDate end);
}
