package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto.MatchResponse;
import is.hi.hbv501gteam23.Services.Interfaces.MatchService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

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
     * @param filter  filter parameters for listing matches
     * @return {@link ResponseEntity} with status 200 (OK) containing a list of
     * {@link MatchDto.MatchResponse} that match the given filters
     */
    @GetMapping
    @Operation(summary = "List matches")
    public List<MatchResponse> listMatches(
        @ParameterObject @ModelAttribute MatchDto.MatchFilter filter
    ) {
        List<Match> matches = matchService.findMatchFilter(filter);
        return matches.stream()
                      .map(this::toResponse)
                      .toList();
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
