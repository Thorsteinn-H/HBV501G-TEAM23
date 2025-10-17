package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Team;

import java.util.List;

public interface TeamService {
    Team findById(long id);
    Team findByName(String name);
    Team findByCountry(String country);
    List<Team> findByVenue(Long venueId);
    List<Team> findAll();
    Team create(Team team);
    Team update(Team team);
    void delete(Long id);

}
