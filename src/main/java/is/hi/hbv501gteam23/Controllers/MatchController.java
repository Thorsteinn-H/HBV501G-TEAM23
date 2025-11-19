package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto.MatchResponse;
import is.hi.hbv501gteam23.Services.Interfaces.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller that exposes read/write operations for {@link Match} resources.
 * Base path is /matches
 */
@Tag(name = "Match")
@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    /**
     * Retrieves a list of matches filtered by the given optional criteria.
     * <p>
     * All parameters are optional; when a parameter is {@code null}, it is ignored in the filter.
     * The result can be sorted by a given field and direction.
     *
     * @param startDate    lower bound for the match date
     * @param endDate      upper bound for the match date
     * @param homeGoals    exact number of goals scored by the home team
     * @param awayGoals    exact number of goals scored by the away team
     * @param homeTeamName name of the home team to filter by
     * @param awayTeamName name of the away team to filter by
     * @param venueName    name of the venue to filter by
     * @param sortBy       field to sort by (defaults to {@code "id"} if not provided)
     * @param sortDir      sort direction, either {@code "ASC"} or {@code "DESC"} (defaults to {@code "ASC"})
     * @return {@link ResponseEntity} with status 200 (OK) containing a list of
     * {@link MatchDto.MatchResponse} that match the given filters
     */
    @GetMapping
    @Operation(summary = "List matches")
    public ResponseEntity<List<MatchDto.MatchResponse>> listMatches(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Integer homeGoals,
            @RequestParam(required = false) Integer awayGoals,
            @RequestParam(required = false) String homeTeamName,
            @RequestParam(required = false) String awayTeamName,
            @RequestParam(required = false) String venueName,
            @Parameter @RequestParam(required = false,defaultValue = "id") String sortBy,
            @Parameter @RequestParam(required = false,defaultValue = "ASC") String sortDir
    )
    {
        List<Match> matches=matchService.findMatchFilter(startDate,endDate,homeGoals,awayGoals,homeTeamName,awayTeamName
                ,venueName,sortBy,sortDir);

        List<MatchDto.MatchResponse> response = matches.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Gets a {@link Match} entity by its ID
     *
     * @param id the id of the match
     * @return a {@link MatchDto.MatchResponse} containing the mapped data
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get match by ID")
    public MatchDto.MatchResponse getMatchById(@PathVariable Long id) {
        return toResponse(matchService.getMatchById(id));
    }

    /**
     * Creates a new match.
     *
     * @param body the match data to create
     * @return {@link ResponseEntity} with status 201 (CREATED) containing the created match
     * mapped to {@link MatchResponse} and a {@code Location} header pointing to /matches/{id}
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a match")
    @ResponseBody
    public ResponseEntity<MatchDto.MatchResponse> createMatch(@RequestBody MatchDto.CreateMatchRequest body) {
        Match createdMatch = matchService.createMatch(body);
        return ResponseEntity.created(URI.create("/matches" + createdMatch.getId())).body(toResponse(createdMatch));
    }

    /**
     * Partially updates an existing match.
     *
     * @param id   the ID of the match to update
     * @param body the fields to update
     * @return {@link ResponseEntity} with status 200 (OK) containing the updated match
     * mapped to {@link MatchResponse}
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Modify a match")
    public ResponseEntity<MatchDto.MatchResponse> updateMatch(@PathVariable Long id, @RequestBody MatchDto.PatchMatchRequest body) {
        Match updatedMatch = matchService.patchMatch(id, body);
        return ResponseEntity.ok(toResponse(updatedMatch));
    }

    /**
     * Maps a {@link Match} entity to a {@link MatchDto.MatchResponse} DTO.
     * @param m match entity to map
     * @return mapped {@link MatchDto.MatchResponse}
     */
    private MatchDto.MatchResponse toResponse(Match m) {
        return new MatchDto.MatchResponse(
                m.getId(),
                m.getMatchDate(),
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
