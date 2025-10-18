package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto;
import is.hi.hbv501gteam23.Services.Interfaces.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto.TeamResponse;


import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;


    @GetMapping
    public List<TeamResponse> getAllTeams(){
        return teamService.getAllTeams()
                .stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public TeamResponse getTeamById(@PathVariable Long id){
        return toResponse(teamService.getTeamById(id));
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
        Team team = teamService.getTeamById(id);
        if(team!=null){
            return;
        }
        teamService.deleteByid(id);

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

    @GetMapping("/by-venue/{venueId}")
    public List<Team> getByVenueId(@PathVariable Long id){
        return teamService.findByVenueId(id);
    }

    @GetMapping("/by-country/{country}")
    public Team getByCountry(@PathVariable String country){
        return teamService.findByCountry(country);
    }

    private TeamDto.TeamResponse toResponse(Team t) {
        return new TeamDto.TeamResponse(
                t.getId(),
                t.getName(),
                t.getCountry(),
                t.getVenue().getId(),
                t.getVenue().getName()
        );
    }
}
