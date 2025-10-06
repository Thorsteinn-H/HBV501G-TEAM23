package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Team;
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
public class TeamController {
    private TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }
    public static List<Team> getAllTeams(){
        return null;
    }

    public static Team getTeam(Long id){
        return null;
    }

    public static Team createTeam(Team team){
        return null;
    }

    public static void deleteTeam(Long id){

    }

    public static Team updateTeam(Team team){
        return null;
    }

    public static List<Team> getByVenue(Long id){
        return null;
    }
}
