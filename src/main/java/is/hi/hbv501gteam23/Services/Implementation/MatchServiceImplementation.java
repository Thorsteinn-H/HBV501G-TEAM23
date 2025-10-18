package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.Repositories.MatchRepository;
import is.hi.hbv501gteam23.Services.Interfaces.MatchService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImplementation implements MatchService {
    private final MatchRepository matchRepository;

    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match getMatchById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match " + id + " not found"));
    }

    @Override
    public List<Match> getMatchesByTeamId(Long teamId) {
        return matchRepository.findByHomeTeam_IdOrAwayTeam_Id(teamId, teamId);
    }

    @Override
    public List<Match> getMatchesByYear(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end   = LocalDate.of(year, 12, 31);
        return matchRepository.findByDateBetween(start, end);
    }

    @Override
    public Match updateMatch(Match match) {
        if (match.getId() == null || !matchRepository.existsById(match.getId())) {
            throw new EntityNotFoundException("Match " + match.getId() + " not found");
        }
        return matchRepository.save(match);
    }

    @Override
    public Match createMatch(Match match) {
        match.setId(null);
        return matchRepository.save(match);
    }

    @Override
    public void deleteMatch(Long id) {
        matchRepository.deleteById(id);
    }
}
