package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto;
import is.hi.hbv501gteam23.Services.Interfaces.TeamService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/name={name}")
    public TeamResponse getTeamByName(@PathVariable("name") String name) {
        Team team = teamService.findByName(name); // returns Team (or Optional<Team>)
        return toResponse(team);
    }

    @GetMapping("/venue/{venueId}")
    public List<TeamResponse> getByVenueId(@PathVariable("venueId") Long venueId) {
        return teamService.findByVenueId(venueId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/country={country}")
    public List<TeamResponse> getByCountry(@PathVariable("country") String country) {
        return teamService.findByCountry(country)
                .stream()
                .map(this::toResponse)
                .toList();
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
