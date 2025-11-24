package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto.TeamResponse;
import is.hi.hbv501gteam23.Services.Interfaces.MetadataService;
import is.hi.hbv501gteam23.Services.Interfaces.TeamService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Collections;
import java.util.List;

/**
 * REST controller that exposes read/write operations for {@link Team} resources.
 * Base path is /teams
 */
@Tag(name = "Team")
@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    private final MetadataService metadataService;

    /**
     * Retrieves a list of teams filtered by the given optional criteria.
     * <p>
     * All parameters are optional; when a parameter is {@code null}, it is ignored in the filter.
     * If a country is provided, it must be one of the configured countries in {@link MetadataService},
     * otherwise a 400 (Bad Request) response is returned with an empty list.
     *
     * @param filter  filter for filtering and sorting params
     * @return {@link ResponseEntity} with status 200 (OK) containing a list of {@link TeamResponse},
     * or 400 (Bad Request) with an empty list if the country is invalid
     */
    @GetMapping
    @Operation(summary = "List teams")
    public ResponseEntity<List<TeamDto.TeamResponse>> listTeams(
            @ParameterObject @ModelAttribute TeamDto.TeamFilter filter
    ) {
        if (filter != null && filter.country() != null) {
            boolean validCountry = metadataService.getAllCountries().stream()
                    .anyMatch(c -> c.value().equalsIgnoreCase(filter.country()));
            if (!validCountry) {
                return ResponseEntity
                        .badRequest()
                        .body(Collections.emptyList());
            }
        }
        List<Team> teams=teamService.listTeams(filter);
        List<TeamDto.TeamResponse> response = teams.stream()
                .map(this::toResponse)
                .toList();

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
     * @return {@link ResponseEntity} with status 201 (CREATED) containing the created team
     * mapped to {@link TeamResponse} and a {@code Location} header pointing to
     * /teams/{id}
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
     * @return {@link ResponseEntity} with status 200 (OK) containing the updated team
     * mapped to {@link TeamResponse}
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
     * <p>
     * If a team has no venue or the venue has no non-blank name, the venue name
     * defaults to the string {@code "Enginn heimavöllur"}.
     *
     * @param t the team entity to map
     * @return the mapped {@link TeamDto.TeamResponse}
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
                t.getCountry() != null ? t.getCountry().getCode() : null,
                venueId,
                venueName
        );
    }
}
