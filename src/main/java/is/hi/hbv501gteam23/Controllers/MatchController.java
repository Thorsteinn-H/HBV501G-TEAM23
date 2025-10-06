package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Services.Interfaces.MatchService;
import is.hi.hbv501gteam23.Services.Interfaces.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
/**
 * Nafn : Þorsteinn H. Erlendsson
 * Tölvupóstur: the85@hi.is
 * Lýsing:
 **/

@Controller
public class MatchController {
    private MatchService matchService;

    @Autowired
    public MatchController(MatchService MatchService){
        this.matchService = MatchService;
    }

    public static List<Match> getAllMatches(){
        return null;
    }

    public static Match getMatchById(Long id){
        return null;
    }

    public static Match createMatch(Match match){
        return null;
    }

    public static Match updateMatch(Match match){
        return null;
    }

    public static void deleteMatch(Match match){

    }

    public static List<Match> getMatchesByYear(int year){
        return null;
    }

    public static List<Match> getMatchesByTeam(Long team){
        return null;
    }

    public static List<Match> getMatchesByPlayer(Long player){
        return null;
    }
}
