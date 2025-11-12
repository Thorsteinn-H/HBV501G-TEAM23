package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto.MatchResponse;
import is.hi.hbv501gteam23.Services.Interfaces.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
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
     * Lists all {@link Match} entities
     *
     * @return list of matches mapped to {@link MatchResponse}
     */
    @GetMapping
    @Operation(summary = "List all matches", description = "Lists all matches with optional filters by date or team.")
    public List<MatchResponse> getAllMatches() {
        return matchService.getAllMatches()
            .stream()
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
     * Returns a list of matches that played during a specific year
     *
     * @param from the year to filter matches
     * @return a list of matches mapped to {@link MatchResponse}
     */
    @GetMapping(params = {"from","to"})
    @Operation(summary = "Get matches between time period")
    public List<MatchResponse> getMatchesBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return matchService.getMatchesBetween(from, to).stream().map(this::toResponse).toList();
    }

    /**
     * Retrieves all matches that a given team played.
     *
     * @param teamId the id of the team the players should belong to.
     * @return a list of matches mapped to {@link MatchResponse}
     */
    @GetMapping(params = "team")
    @Operation(summary = "List all matches played by a team")
    public List<MatchResponse> getMatchesByTeam(@RequestParam Long teamId) {
        return matchService.getMatchesByTeamId(teamId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Creates a new match.
     *
     * @param body the match data to create
     * @return the created match mapped to {@link MatchResponse}
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
     * Updates an existing match.
     *
     * @param id the id of the match to update
     * @param body the fields to update
     * @return the updated match mapped to {@link MatchResponse}
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Modify a match")
    public ResponseEntity<MatchDto.MatchResponse> updateMatch(@PathVariable Long id, @RequestBody MatchDto.PatchMatchRequest body) {
        Match updatedMatch = matchService.patchMatch(id, body);
        return ResponseEntity.ok(toResponse(updatedMatch));
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
