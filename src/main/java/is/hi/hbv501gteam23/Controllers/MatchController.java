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

    @GetMapping
    @Operation(summary = "Filter teams")
    public ResponseEntity<List<MatchDto.MatchResponse>> filterMatch(
            @RequestParam(required = false) Long matchId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Integer homeGoals,
            @RequestParam(required = false) Integer awayGoals,
            @RequestParam(required = false) Long homeTeamId,
            @RequestParam(required = false) String homeTeamName,
            @RequestParam(required = false) Long awayTeamId,
            @RequestParam(required = false) String awayTeamName,
            @RequestParam(required = false) Long venueId,
            @RequestParam(required = false) String venueName,
            @Parameter @RequestParam(required = false,defaultValue = "id") String sortBy,
            @Parameter @RequestParam(required = false,defaultValue = "ASC") String sortDir
    )
    {

        List<Match> matches=matchService.findMatchFilter(matchId,startDate,endDate,homeGoals,awayGoals,homeTeamId,homeTeamName,awayTeamId,awayTeamName,
                venueId,venueName,sortBy,sortDir);

        List<MatchDto.MatchResponse> response = matches.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
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
