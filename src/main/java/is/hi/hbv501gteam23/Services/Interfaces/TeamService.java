package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Team;

import java.util.List;

public interface TeamService {

    /**
     * Retrieves all teams
     *
     * @return a list of all {@link Team} entities
     */
    List<Team> getAllTeams();

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
     * Retrieves all team in with a specific venue
     *
     * @param venueId the id of the team
     * @return a list of {@link Team} entities involving the specified team
     */
    List<Team> findByVenueId(Long venueId);

    /**
     * Creates a new team
     *
     * @param team the {@link Team} entity to create
     * @return the newly created {@link Team} entity
     */
    Team create(Team team);

    /**
     * Updates an existing team with new data
     *
     * @param team the {@link Team} entity with updated fields
     * @return the updated {@link Team} entity
     */
    Team update(Team team);

    /**
     * Deletes a team by its id
     *
     * @param id the id of the team to delete
     */
    void deleteTeam(Long id);

}
