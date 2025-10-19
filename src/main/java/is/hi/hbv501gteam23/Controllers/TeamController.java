package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto;
import is.hi.hbv501gteam23.Services.Interfaces.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto.TeamResponse;


import java.util.List;

/**
 * REST controller that exposes read/write operations for {@link Team} resources.
 * Base path is api/players
 * <p>Other endpoints (search, create, update, delete) support administration and UX helpers.</p>
 */
@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;


    /**
     * Retrieves all teams.
     *
     * <p>
     *      This method retrieves all {@link Team} entities from the database
     * </p>
     *
     * @return list of teams mapped to {@link TeamResponse}
     */
    @GetMapping
    public List<TeamResponse> getAllTeams(){
        return teamService.getAllTeams()
                .stream().map(this::toResponse).toList();
    }

    /**
     * Retrieves a single team by id
     *
     * <p>
     *      This method retrieves a {@link Team} entity from the database with a specific identifier.
     * </p>
     *
     * @param id the id of the team to be retrieved
     * @return the team mapped to a {@link TeamResponse}
     */
    @GetMapping("/{id}")
    public TeamResponse getTeamById(@PathVariable Long id){
        return toResponse(teamService.getTeamById(id));
    }

    /**
     * Creates a new team.
     *
     * <p>
     *     This method saves a new {@link Team} entity to the database.
     * </p>
     *
     * @param team the {@link Team} object to be created
     * @return the created {@link Team} entity
     */
    @PostMapping
    public Team createTeam(Team team){
        if(teamService.findByName(team.getName())!=null)
        {
            return null;
        }
        return teamService.create(team);
    }

    /**
     * Deletes a team.
     *
     * <p>
     *     This method deletes a {@link Team} entity in the database.
     * </p>
     *
     * @param id the id of the team to be deleted.
     *
     */
    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id){
        Team team = teamService.getTeamById(id);
        if(team!=null){
            return;
        }
        teamService.deleteByid(id);

    }

    /**
     * Updates a team
     *
     * <p>
     *     This method updates a {@link Team} entity in the database.
     * </p>
     * @param team the {@link Team} object to be updated.
     * @return the updated {@link Team} entity
     */
    @PutMapping("/{team}")
    public Team updateTeam(@PathVariable Team team){
        Long id = team.getId();
        if(id!=null){
            return null;
        }
        return teamService.update(team);
    }

    /**
     * Retrieves team by name
     *
     * <p>
     *       This method retrieves a {@link Team} entity from the database with a specific name.
     * </p>
     * @param name name of the team to be retrieved
     * @return the team mapped to a {@link TeamResponse}
     */
    @GetMapping("/name={name}")
    public TeamResponse getTeamByName(@PathVariable("name") String name) {
        return toResponse(teamService.findByName(name));
    }

    /**
     * Retrieves a list of teams by their venue.
     *
     * <p>
     *       This method retrieves a list of {@link Team} entities from the database
     *       with a specific venue.
     * </p>
     * @param venueId the id of the venue of the teams to be retrieved.
     * @return list of teams mapped to {@link TeamResponse}
     */
    @GetMapping("/venue/{venueId}")
    public List<TeamResponse> getByVenueId(@PathVariable("venueId") Long venueId) {
        return teamService.findByVenueId(venueId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Retrieves a list of teams by their country.
     *
     * <p>
     *      This method retrieves a list of {@link Team} entities from the database
     *      from a specific country.
     * </p>
     * @param country the country of the teams to be retrieved.
     * @return list of teams mapped to {@link TeamResponse}
     */
    @GetMapping("/country={country}")
    public List<TeamResponse> getByCountry(@PathVariable("country") String country) {
        return teamService.findByCountry(country)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Maps a {@link Team} entity to a {@link TeamDto.TeamResponse} DTO.
     * @param t team entity
     * @return mapped {@link TeamDto.TeamResponse}
     */
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
