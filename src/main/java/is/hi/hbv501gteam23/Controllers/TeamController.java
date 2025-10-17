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


    @GetMapping
    public List<Team> getAllTeams(){
        return teamService.findAll();
    }

    @GetMapping("/{id}")
    public Team getTeam(@PathVariable Long id){
        return teamService.findById(id);
    }

    @PostMapping
    public Team createTeam(Team team){
        if(teamService.findByName(team.getName())!=null)
        {
            return null;
        }
        return teamService.create(team);
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id){
        Team team = teamService.findById(id);
        if(team!=null){
            //Print ekkert team til að eyða
            return;
        }
        //Liði eytt
        teamService.delete(id);

    }

    @PutMapping("/{team}")
    public Team updateTeam(@PathVariable Team team){
        Long id = team.getId();
        if(id!=null){
            //Print ekkert team til að uppfæra
            return null;
        }
        return teamService.update(team);
    }

    @GetMapping("/{venue}")
    public List<Team> getByVenue(@PathVariable Long id){
        return teamService.findByVenue(id);
    }

    @GetMapping("/{country}")
    public Team getByCountry(@PathVariable String country){
        return teamService.findByCountry(country);
    }
}
