package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto.MatchResponse;
import is.hi.hbv501gteam23.Services.Interfaces.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @GetMapping
    public List<MatchResponse> getAllMatches() {
        return matchService.getAllMatches()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public MatchDto.MatchResponse getMatchById(@PathVariable Long id) {
        return toResponse(matchService.getMatchById(id));
    }

    @PostMapping
    public Match createMatch(Match match){
        return matchService.createMatch(match);
    }

    @PutMapping("/{match}")
    public Match updateMatch(@PathVariable Match match){
        return matchService.updateMatch(match);
    }

    @DeleteMapping("/{id}")
    public void deleteMatch(@PathVariable Long id){
        matchService.deleteMatch(id);
    }

    @GetMapping("/year={year}")
    public List<MatchResponse> getMatchesByYear(@PathVariable int year) {
        return matchService.getMatchesByYear(year)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/team/{teamId}")
    public List<MatchResponse> getMatchesByTeam(@PathVariable Long teamId) {
        return matchService.getMatchesByTeamId(teamId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

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
