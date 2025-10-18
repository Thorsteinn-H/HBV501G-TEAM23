package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Team;

import java.util.List;

public interface TeamService {
    List<Team> getAllTeams();
    Team getTeamById(Long id);
    Team findByName(String name);
    Team findByCountry(String country);
    List<Team> findByVenueId(Long venueId);
    Team create(Team team);
    Team update(Team team);
    void deleteByid(Long id);

}
