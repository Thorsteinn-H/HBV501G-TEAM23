package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto.MatchResponse;
import is.hi.hbv501gteam23.Services.Interfaces.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller that exposes read/write operations for {@link Match} resources.
 * Base path is /matches
 */
@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    /**
     * Retrieves all matches
     *
     * <p>
     *      This method retrieves all {@link Match} entities from the database
     * </p>
     *
     * @return list of matches mapped to {@link MatchResponse
     */
    @GetMapping
    public List<MatchResponse> getAllMatches() {
        return matchService.getAllMatches()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Retrieves a match based on it's id
     *
     * <p>
     *       This method retrieves a {@link Match} entity from the database
     *       with a specific identifier.
     * </p>
     *
     * @param id the id of the match
     * @return a {@link MatchDto.MatchResponse} containing the mapped data
     */
    @GetMapping("/{id}")
    public MatchDto.MatchResponse getMatchById(@PathVariable Long id) {
        return toResponse(matchService.getMatchById(id));
    }

    /**
     * Retrieves all matches that were played during a specific year
     *
     * <p>This endpoint return a list of matches that played during a specific year</p>
     *
     * @param year the year to filter matches
     * @return a list of matches mapped to {@link MatchResponse}
     */
    @GetMapping("/year={year}")
    public List<MatchResponse> getMatchesByYear(@PathVariable int year) {
        return matchService.getMatchesByYear(year)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Retrieves all matches that a given team played.
     *
     * <p>
     *     This endpoint return a list of matches where a team identified with a team id has
     *      participated inn
     * </p>
     *
     * @param teamId the id of the team the players should belong to.
     * @return a list of matches mapped to {@link MatchResponse}
     */
    @GetMapping("/team/{teamId}")
    public List<MatchResponse> getMatchesByTeam(@PathVariable Long teamId) {
        return matchService.getMatchesByTeamId(teamId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Maps a {@link Match} entity to a {@link MatchDto.MatchResponse} DTO.
     * @param m match entity
     * @return mapped {@link MatchDto.MatchResponse}
     */
    private MatchDto.MatchResponse toResponse(Match m) {
        return new MatchDto.MatchResponse(
                m.getId(),
                m.getDate(),
                m.getHomeTeam() != null ? m.getHomeTeam().getId()   : null,
                m.getHomeTeam() != null ? m.getHomeTeam().getName() : null,
                m.getAwayTeam() != null ? m.getAwayTeam().getId()   : null,
                m.getAwayTeam() != null ? m.getAwayTeam().getName() : null,
                m.getVenue()    != null ? m.getVenue().getId()      : null,
                m.getVenue()    != null ? m.getVenue().getName()    : null,
                m.getHomeGoals(),
                m.getAwayGoals()
        );
    }
}
