package is.hi.hbv501gteam23.Persistence.Repositories;


import is.hi.hbv501gteam23.Persistence.Entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for accessing and managing {@link Team} entities.
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    /**
     * Retrieves a team by name and ignores case
     * @param name the name of the team
     * @return A team with a specific name
     */
    Team findByNameContainingIgnoreCase(String name);

    /**
     * Retrieves all teams from a specific country
     * @param country the country the team is from
     * @return List of all teams from the same country
     */
    List<Team> findAllByCountryIgnoreCase(String country);
    boolean existsByCountryIgnoreCase(String country);

    /**
     * Retrieves all teams with same venue
     * @param venueId id of the teams venue
     * @return List of all teams that have teamId as a venue
     */
    @Query("SELECT t FROM Team t WHERE t.venue.id = :venueId")
    List<Team> findByVenueId(Long venueId);

    /**
     * Retrieves all teams with a specific active status
     * @param isActive the active status of a team
     * @return List of all teams that have the same active status
     */
    List<Team> findByIsActive(Boolean isActive);
}
