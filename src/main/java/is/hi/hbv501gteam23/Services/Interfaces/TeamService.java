package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto;
import java.util.List;

public interface TeamService {

    /**
     * Finds teams using optional filters, with sorting.
     * <p>
     * All filter parameters are optional; when {@code null}, they are ignored.
     * Supports filtering by name, active status, country and venue name.
     *
     * @param filter filter and sort parameters
     * @return list of {@link Team} entities matching the given filters
     */
    List<Team> listTeams(TeamDto.TeamFilter filter);

    /**
     * Retrieves a single team by its unique identifier.
     *
     * @param id the id of the team
     * @return the {@link Team} with the specified id
     */
    Team getTeamById(Long id);

    /**
     * Retrieves all teams in a specific venue
     *
     * @param venueId the id of the team
     * @return a list of {@link Team} entities involving the specified team
     */
    List<Team> findByVenueId(Long venueId);

    /**
     * Creates a new team
     *
     * @param body the {@link Team} entity to create
     * @return the newly created {@link Team} entity
     */
    Team createTeam(TeamDto.CreateTeamRequest body);
    /**
     * Updates existing team
     * @param id the id of the team to update
     * @param body partial update payload for the team
     * @return 200 OK with the updated {@link TeamDto.TeamResponse};
     */
    Team patchTeam(Long id, TeamDto.PatchTeamRequest body);

    /**
     * Deletes a team by its id
     *
     * @param id the id of the team to delete
     */
    void deleteTeam(Long id);

}
