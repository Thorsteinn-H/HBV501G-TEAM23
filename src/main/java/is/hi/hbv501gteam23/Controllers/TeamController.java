package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto.TeamResponse;
import is.hi.hbv501gteam23.Services.Interfaces.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes read/write operations for {@link Team} resources.
 * Base path is /players
 */
@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    /**
     * Retrieves all {@link Team} entities.
     * @return list of teams mapped to {@link TeamResponse}
     */
    @Operation(summary = "List all teams")
    @ApiResponse(responseCode = "200", description = "Teams successfully fetched")
    @GetMapping
    public List<TeamResponse> getAllTeams(){
        return teamService.getAllTeams()
                .stream().map(this::toResponse).toList();
    }

    /**
     * Retrieves a {@link Team} entity by its id
     * @param id the id of the team to be retrieved
     * @return the team mapped to a {@link TeamResponse}
     */
    @Operation(summary = "Get team by id")
    @ApiResponse(responseCode = "200", description = "Team successfully fetched")
    @GetMapping("/{id}")
    public TeamResponse getTeamById(@PathVariable Long id){
        return toResponse(teamService.getTeamById(id));
    }

    /**
     * Retrieves a {@link Team} entity by name
     * @param name name of the team to be retrieved
     * @return the team mapped to a {@link TeamResponse}
     */
    @GetMapping(params = "name")
    public TeamResponse getTeamByName(@RequestParam String name) {
        return toResponse(teamService.findByName(name));
    }

    /**
     *
     * @param isActive the active status of a team
     * @return
     */
    @GetMapping("/isActive={isActive}")
    public List<TeamResponse> getActiveTeams(@PathVariable("isActive") Boolean isActive) {
        return teamService.findByActiveStatus(isActive)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Retrieves a list of {@link Team} entities by venue.
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
     * Retrieves a list of {@link Team} entities from a specific country.
     * @param country the country of the teams to be retrieved.
     * @return list of teams mapped to {@link TeamResponse}
     */
    @GetMapping(params = "country")
    public List<TeamResponse> getTeamByCountry(@RequestParam String country) {
        return teamService.findByCountry(country)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Creates a new team.
     * @param createTeamRequest the team data to create
     * @return the created team mapped to {@link TeamResponse}
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a team")
    @ApiResponse(responseCode = "200", description = "Team successfully created")
    public TeamResponse createTeam(@RequestBody TeamDto.CreateTeamRequest createTeamRequest) {
        Team createdTeam = teamService.createTeam(createTeamRequest);
        return toResponse(createdTeam);
    }

    /**
     * Updates an existing team. Team can be marked as inactive with isActive = false.
     * @param id the id of the team to update
     * @param patchTeamRequest the fields to update
     * @return the updated team mapped to {@link TeamResponse}
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Modify a team")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponse(responseCode = "200", description = "Team successfully modified")
    public TeamResponse patchTeam(@PathVariable Long id, @RequestBody TeamDto.PatchTeamRequest patchTeamRequest) {
        Team updatedTeam = teamService.patchTeam(id, patchTeamRequest);
        return toResponse(updatedTeam);
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
                t.isActive(),
                t.getCountry(),
                t.getVenue().getId(),
                t.getVenue().getName()
        );
    }
}
