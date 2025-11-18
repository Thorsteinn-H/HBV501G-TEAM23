package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.Repositories.MatchRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.TeamRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.VenueRepository;
import is.hi.hbv501gteam23.Persistence.Specifications.MatchSpecification;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto;
import is.hi.hbv501gteam23.Services.Interfaces.MatchService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImplementation implements MatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;

    /**
     * Retrieves a list of matches filtered by the given optional criteria and sorted by the given field.
     * <p>
     * All filter parameters are optional; when {@code null}, they are ignored in the specification.
     *
     * @param startDate    lower bound for the match date
     * @param endDate      upper bound for the match date
     * @param homeGoals    exact number of goals scored by the home team
     * @param awayGoals    exact number of goals scored by the away team
     * @param homeTeamName home team name filter
     * @param awayTeamName away team name filter
     * @param venueName    venue name filter
     * @param sortBy       field to sort by
     * @param sortDir      sort direction, either {@code "ASC"} or {@code "DESC"}
     * @return list of {@link Match} entities matching the given filters
     */
    @Override
    public List<Match> findMatchFilter(LocalDate startDate,
                                       LocalDate endDate,Integer homeGoals,
                                       Integer awayGoals,
                                       String homeTeamName,
                                       String awayTeamName,
                                       String venueName, String sortBy, String sortDir) {

        Specification<Match> spec= Specification.allOf(
                MatchSpecification.matchDate(startDate,endDate),
                MatchSpecification.matchHomeGoals(homeGoals),
                MatchSpecification.matchAwayGoals(awayGoals),
                MatchSpecification.matchHomeTeamName(homeTeamName),
                MatchSpecification.matchAwayTeamName(awayTeamName),
                MatchSpecification.matchVenueName(venueName)
        );

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        return matchRepository.findAll(spec,sort);
    }

    /**
     * Retrieves a single match by its unique identifier.
     *
     * @param id the id of the match
     * @return the {@link Match} with the specified id
     * @throws ResponseStatusException with status 404 if the match is not found
     */
    @Override
    public Match getMatchById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match " + id + " not found"));
    }

    /**
     * Retrieves all matches in which a specific team has participated.
     *
     * @param teamId the ID of the team
     * @return a list of {@link Match} entities involving the specified team
     * @throws ResponseStatusException with status 400 if {@code teamId} is null
     * @throws ResponseStatusException with status 404 if the team does not exist
     */
    @Override
    public List<Match> getMatchesByTeamId(Long teamId) {
        if (teamId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "teamId is required");
        }
        if (!teamRepository.existsById(teamId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team " + teamId + " not found");
        }
        return matchRepository.findByHomeTeam_IdOrAwayTeam_Id(teamId, teamId);
    }

    /**
     * Retrieves all matches played between the given dates (inclusive), ordered by date ascending.
     *
     * @param from the start date (inclusive)
     * @param to   the end date (inclusive)
     * @return list of {@link Match} entities whose date is between {@code from} and {@code to}
     * @throws ResponseStatusException with status 400 if either date is null
     * @throws ResponseStatusException with status 400 if {@code to} is before {@code from}
     */
    @Override
    public List<Match> getMatchesBetween(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "from and to are required");
        }
        if (to.isBefore(from)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'to' must be on/after 'from'");
        }
        return matchRepository.findByMatchDateBetweenOrderByMatchDateAsc(from, to); // inclusive
    }

    /**
     * Partially updates a {@link Match} identified by {@code id}.
     * <p>
     * Applies only the non-null fields from {@code body}. Supported fields:
     * {@code matchDate}, {@code homeTeamId}, {@code awayTeamId}, {@code venueId},
     * {@code homeGoals}, {@code awayGoals}. Team and venue identifiers (if present)
     * are looked up and validated before being set.
     *
     * @param id   the id of the match to update
     * @param body partial update payload
     * @return the updated {@link Match}
     *
     * @throws EntityNotFoundException
     *         if the match does not exist, or if any referenced team/venue id in the
     *         payload cannot be found
     */
    @Override
    @Transactional
    public Match patchMatch(Long id, MatchDto.PatchMatchRequest body) {
        Match m = matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match " + id + " not found"));

        if (body.matchDate() != null)      m.setMatchDate(body.matchDate());
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
     * Creates a new match from the provided request body.
     * <p>
     * Validates that required IDs and date are present, that team IDs are different, and
     * that goal values (if provided) are non-negative. Also checks that referenced teams
     * and venue exist.
     *
     * @param body the {@link MatchDto.CreateMatchRequest} containing match details
     * @return the created {@link Match}
     *
     * @throws ResponseStatusException with status 400 if required fields are missing
     *                                 or invalid
     * @throws ResponseStatusException with status 404 if referenced teams or venue are not found
     */
    @Override
    @Transactional
    public Match createMatch(MatchDto.CreateMatchRequest body) {
        if (body == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required");
        }
        if (body.homeTeamId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "homeTeamId is required");
        }
        if (body.awayTeamId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "awayTeamId is required");
        }
        if (body.venueId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "venueId is required");
        }
        if (body.matchDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "matchDate is required");
        }

        if (body.homeTeamId().equals(body.awayTeamId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "homeTeamId and awayTeamId must be different");
        }

        if (body.homeGoals() != null && body.homeGoals() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "homeGoals must be >= 0");
        }
        if (body.awayGoals() != null && body.awayGoals() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "awayGoals must be >= 0");
        }

        Team home = teamRepository.findById(body.homeTeamId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Home team " + body.homeTeamId() + " not found"));

        Team away = teamRepository.findById(body.awayTeamId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Away team " + body.awayTeamId() + " not found"));

        Venue venue = venueRepository.findById(body.venueId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue " + body.venueId() + " not found"));

        Match m = new Match();
        m.setHomeTeam(home);
        m.setAwayTeam(away);
        m.setVenue(venue);
        m.setMatchDate(body.matchDate());
        m.setHomeGoals(body.homeGoals() != null ? body.homeGoals() : 0);
        m.setAwayGoals(body.awayGoals() != null ? body.awayGoals() : 0);
        return matchRepository.save(m);
    }

    /**
     * Deletes a match by its id.
     *
     * @param id the id of the match to delete
     */
    @Override
    public void deleteMatch(Long id) {
        matchRepository.deleteById(id);
    }
}
