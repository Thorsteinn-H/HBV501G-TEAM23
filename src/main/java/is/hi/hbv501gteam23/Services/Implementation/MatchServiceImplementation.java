package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.Repositories.MatchRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.TeamRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.VenueRepository;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto;
import is.hi.hbv501gteam23.Services.Interfaces.MatchService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImplementation implements MatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;

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
     * Partially updates a {@link Match} identified by {@code id}.
     * Applies only the non-null fields from {@code body}. Supported fields:
     * {@code date}, {@code homeTeamId}, {@code awayTeamId}, {@code venueId},
     * {@code homeGoals}, {@code awayGoals}. Team and venue identifiers (if present)
     * are looked up and validated before being set.
     *
     * @param id   the id of the match to update
     * @param body partial update payload
     * @return the updated {@link Match}
     *
     * @throws jakarta.persistence.EntityNotFoundException
     *  *         if the match does not exist, or if any referenced team/venue id in the
     *  *         payload cannot be found
     */
    @Override
    @Transactional
    public Match patchMatch(Long id, MatchDto.PatchMatchRequest body) {
        Match m = matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match " + id + " not found"));

        if (body.date() != null)      m.setDate(body.date());
        if (body.homeGoals() != null) m.setHomeGoals(body.homeGoals());
        if (body.awayGoals() != null) m.setAwayGoals(body.awayGoals());
        if (body.homeTeamId() != null) {
            Team home = teamRepository.findById(body.homeTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team " + body.homeTeamId() + " not found"));
            m.setHomeTeam(home);
        }
        if (body.awayTeamId() != null) {
            Team away = teamRepository.findById(body.awayTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team " + body.awayTeamId() + " not found"));
            m.setAwayTeam(away);
        }
        if (body.venueId() != null) {
            Venue v = venueRepository.findById(body.venueId())
                    .orElseThrow(() -> new EntityNotFoundException("Venue " + body.venueId() + " not found"));
            m.setVenue(v);
        }

        return matchRepository.save(m);
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
