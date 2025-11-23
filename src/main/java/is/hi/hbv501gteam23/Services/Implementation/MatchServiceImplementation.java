package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.Repositories.MatchRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.TeamRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.VenueRepository;
import is.hi.hbv501gteam23.Persistence.Specifications.MatchSpecifications;
import is.hi.hbv501gteam23.Persistence.Specifications.SpecificationBuilder;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImplementation implements MatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;

    /**
     * Finds matches using optional filters.
     *
     * @param filter  filter parameters for listing matches
     * @return a list of {@link Match} entities matching the filters
     */
    @Override
    public List<Match> findMatchFilter(MatchDto.MatchFilter filter) {
        SpecificationBuilder<Match> builder = new SpecificationBuilder<>();

        if (filter != null) {
            builder.and(MatchSpecifications.matchDate(filter.startDate(), filter.endDate()))
                    .and(MatchSpecifications.matchHomeGoals(filter.homeGoals()))
                    .and(MatchSpecifications.matchAwayGoals(filter.awayGoals()))
                    .and(MatchSpecifications.matchHomeTeamName(filter.homeTeamName()))
                    .and(MatchSpecifications.matchAwayTeamName(filter.awayTeamName()))
                    .and(MatchSpecifications.matchVenueName(filter.venueName()));
        }
        Specification<Match> spec = builder.build();
        Sort sort = buildMatchSort(
            filter != null ? filter.sortBy() : null,
            filter != null ? filter.sortDir() : null
        );
        return matchRepository.findAll(spec, sort);
    }

    private Sort buildMatchSort(String sortBy, String sortDir) {
        String key = sortBy == null ? "" : sortBy.trim();

        String property = switch (key) {
            case "matchDate"    -> "matchDate";
            case "homeGoals"    -> "homeGoals";
            case "awayGoals"    -> "awayGoals";
            case "homeTeamName" -> "homeTeam.name";
            case "awayTeamName" -> "awayTeam.name";
            case "venueName"    -> "venue.name";
            default             -> "id";
        };

        Sort.Direction direction;
        if (sortDir == null) direction = Sort.Direction.ASC;
        else if (sortDir.equalsIgnoreCase("desc")) direction = Sort.Direction.DESC;
        else direction = Sort.Direction.ASC;

        return Sort.by(direction, property);
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

        if (body.matchDate() != null) m.setMatchDate(body.matchDate());
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
