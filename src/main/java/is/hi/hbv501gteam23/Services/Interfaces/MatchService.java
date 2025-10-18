package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Match;

import java.util.List;

public interface MatchService {
    List<Match> getAllMatches();
    Match getMatchById(Long id);
    List<Match> getMatchesByTeamId(Long teamId);
    List<Match> getMatchesByYear(int year);
    Match updateMatch(Match match);
    Match createMatch(Match match);
    void deleteMatch(Long id);
}
