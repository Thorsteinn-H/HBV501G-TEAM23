package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Services.Interfaces.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    /*
    @GetMapping
    public List<Team> getAllTeams(){
        return teamService.listAll();
    }

    @GetMapping("/{id}")
    public Team getTeam(@PathVariable Long id){
        return teamService.getById(id);
    }

    @PostMapping
    public Team createTeam(Team team){
        return teamService.create(team);
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id){
        teamService.delete(id);

    }

    @PutMapping("/{team}")
    public Team updateTeam(@PathVariable Team team){
        return teamService.update(team);
    }

    @GetMapping("/{venue}")
    public List<Team> getByVenue(@PathVariable Long id){
        return teamService.getByVenue(id);
    }
    */
}
