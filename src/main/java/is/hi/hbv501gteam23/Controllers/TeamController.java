package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto.TeamResponse;
import is.hi.hbv501gteam23.Services.Interfaces.MetadataService;
import is.hi.hbv501gteam23.Services.Interfaces.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller that exposes read/write operations for {@link Team} resources.
 * Base path is /players
 */
@Tag(name = "Team")
@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    private final MetadataService metadataService;


    @GetMapping
    @Operation(summary = "Filter teams")
    public ResponseEntity<List<TeamDto.TeamResponse>> filterTeams(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String venueName,
            @Parameter @RequestParam(required = false,defaultValue = "name") String sortBy,
            @Parameter @RequestParam(required = false,defaultValue = "ASC") String sortDir
    )
    {
        if (country != null) {
            boolean validCountry = metadataService.getAllCountries().stream()
                    .anyMatch(c -> c.value().equalsIgnoreCase(country));
            if (!validCountry) {
                return ResponseEntity
                        .badRequest()
                        .body(Collections.emptyList());
            }
        }

        List<Team> teams=teamService.findTeamFilter(name,isActive,country,venueName,sortBy,sortDir);

        List<TeamDto.TeamResponse> response = teams.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a {@link Team} entity by its id
     * @param id the id of the team to be retrieved
     * @return the team mapped to a {@link TeamResponse}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get team by ID")
    public TeamResponse getTeamById(@PathVariable Long id){
        return toResponse(teamService.getTeamById(id));
    }


    /**
     * Retrieves a list of {@link Team} entities by venue.
     * @param venueId the id of the venue of the teams to be retrieved.
     * @return list of teams mapped to {@link TeamResponse}
     */
    @GetMapping("/venue/{venueId}")
    @Operation(summary = "Get team by venue ID")
    public List<TeamResponse> getByVenueId(@PathVariable("venueId") Long venueId) {
        return teamService.findByVenueId(venueId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Creates a new team.
     * @param body the team data to create
     * @return the created team mapped to {@link TeamResponse}
     */
    @PostMapping
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a team")
    public ResponseEntity<TeamDto.TeamResponse> createTeam(@RequestBody TeamDto.CreateTeamRequest body) {
        Team created = teamService.createTeam(body);
        return ResponseEntity.created(URI.create("/teams" + created.getId())).body(toResponse(created));
    }

    /**
     * Updates an existing team. Team can be marked as inactive with isActive = false.
     * @param id the id of the team to update
     * @param body the fields to update
     * @return the updated team mapped to {@link TeamResponse}
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    @Operation(summary = "Modify a team")
    public ResponseEntity<TeamDto.TeamResponse> updateTeam(@PathVariable Long id, @RequestBody TeamDto.PatchTeamRequest body) {
        Team updatedTeam = teamService.patchTeam(id, body);
        return ResponseEntity.ok(toResponse(updatedTeam));
    }

    /**
     * Maps a {@link Team} entity to a {@link TeamDto.TeamResponse} DTO.
     * @param t team entity
     * @return mapped {@link TeamDto.TeamResponse}
     */
    private TeamDto.TeamResponse toResponse(Team t) {
        var v = t.getVenue();
        Long venueId   = (v != null) ? v.getId() : null;
        String venueName = (v != null && v.getName() != null && !v.getName().isBlank())
                ? v.getName()
                : "Enginn heimavöllur";
        return new TeamDto.TeamResponse(
                t.getId(),
                t.getName(),
                t.isActive(),
                t.getCountry(),
                venueId,
                venueName
        );
    }
}
