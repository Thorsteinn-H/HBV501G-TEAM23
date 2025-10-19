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

    /**
     * Retrieves all matches
     *
     * @return a list of all {@link Match} entities
     */
    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    /**
     * Retrieves a single match by its unique identifier.
     *
     * @param id the id of the match
     * @return the {@link Match} with the specified id
     */
    @Override
    public Match getMatchById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match " + id + " not found"));
    }

    /**
     * Retrieves all matches in which a specific team has participated.
     *
     * @param teamId the ID of the team
     * @return a list of {@link Match} entities involving the specified team
     */
    @Override
    public List<Match> getMatchesByTeamId(Long teamId) {
        return matchRepository.findByHomeTeam_IdOrAwayTeam_Id(teamId, teamId);
    }

    /**
     * Retrieves all matches were played during a specific year.
     *
     * @param year the year to filter matches
     * @return a list of {@link Match} entities played during the given year
     */
    @Override
    public List<Match> getMatchesByYear(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end   = LocalDate.of(year, 12, 31);
        return matchRepository.findByDateBetween(start, end);
    }

    /**
     * Updates an existing match with new data
     *
     * @param match the {@link Match} entity with updated fields
     * @return the updated {@link Match} entity
     */
    @Override
    public Match updateMatch(Match match) {
        if (match.getId() == null || !matchRepository.existsById(match.getId())) {
            throw new EntityNotFoundException("Match " + match.getId() + " not found");
        }
        return matchRepository.save(match);
    }

    /**
     * Creates a new match
     *
     * @param match the {@link Match} entity to create
     * @return the newly created {@link Match} entity
     */
    @Override
    public Match createMatch(Match match) {
        match.setId(null);
        return matchRepository.save(match);
    }

    /**
     * Deletes a match by its id
     *
     * @param id the id of the match to delete
     */
    @Override
    public void deleteMatch(Long id) {
        matchRepository.deleteById(id);
    }
}
