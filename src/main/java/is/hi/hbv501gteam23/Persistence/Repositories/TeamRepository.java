package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByName(String name);
    List<Team> findByVenue_Id(Long venueId);
}
