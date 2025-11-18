package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto;
import java.util.List;

public interface TeamService {

    List<Team> findTeamFilter(
             String name, Boolean isActive,
            String country, String venueName,
            String sortBy, String sortDir
    );

    /**
     * Retrieves a single team by its unique identifier.
     *
     * @param id the id of the team
     * @return the {@link Team} with the specified id
     */
    Team getTeamById(Long id);

    /**
     * Retrieves a single team by its name
     *
     * @param name the name of the team
     * @return the {@link Team} with the specified name
     */
    Team findByName(String name);

    /**
     * Retrieves a single team by its country of origin
     *
     * @param country the teams country of origin
     * @return a list of {@link Team} entities from a specific country
     */
    List<Team> findByCountry(String country);

    /**
     * Retrieves all teams in a specific venue
     *
     * @param venueId the id of the team
     * @return a list of {@link Team} entities involving the specified team
     */
    List<Team> findByVenueId(Long venueId);

    /**
     * Retrieves all teams with the same active status
     *
     * @param isActive the active status of a team
     * @return a list of {@link Team} entities with the same active status
     */
    List<Team> findByActiveStatus(Boolean isActive);

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
