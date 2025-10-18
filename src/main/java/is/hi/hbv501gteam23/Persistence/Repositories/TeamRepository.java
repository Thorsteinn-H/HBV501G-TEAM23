package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByNameContainingIgnoreCase(String name);
    List<Team> getByCountry(String country);

    @Query("SELECT t FROM Team t WHERE t.venue.id = :venueId")
    List<Team> findByVenueId(Long venueId);
}
