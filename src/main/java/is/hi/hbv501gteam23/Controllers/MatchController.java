package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Services.Interfaces.MatchService;
import is.hi.hbv501gteam23.Services.Interfaces.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    /*
    @GetMapping
    public List<Match> getAllMatches(){
        return matchService.listAll();
    }

    @GetMapping("/{id}")
    public Match getMatchById(@PathVariable Long id){
        return matchService.getById(id);
    }

    @PostMapping
    public Match createMatch(Match match){
        return matchService.create(match);
    }

    @PutMapping("/{match}")
    public Match updateMatch(@PathVariable Match match){
        return matchService.update(match);
    }

    @DeleteMapping("/{id}")
    public void deleteMatch(@PathVariable Long id){
        matchService.delete(id);

    }

    @GetMapping("/{year}")
    public List<Match> getMatchesByYear(@PathVariable int year){
        return matchService.getByYear(year);
    }

    @GetMapping("/team/{team}")
    public List<Match> getMatchesByTeam(@PathVariable Long team){
        return matchService.getByTeam(team);
    }

    @GetMapping("/player/{player}")
    public List<Match> getMatchesByPlayer(@PathVariable Long player){
        return matchService.findByPlayerId(player);
    }
    */
}
