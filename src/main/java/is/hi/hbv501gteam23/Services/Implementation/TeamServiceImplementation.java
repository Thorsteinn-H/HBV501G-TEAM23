package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.Repositories.PlayerRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.TeamRepository;
import is.hi.hbv501gteam23.Services.Interfaces.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImplementation implements TeamService {
    private final TeamRepository teamRepository;

    /**
     * Retrieves all teams
     *
     * @return a list of all {@link Team} entities
     */
    @Override
    public List<Team> getAllTeams(){
        return teamRepository.findAll();
    }

    /**
     * Retrieves a single team by its unique identifier.
     *
     * @param id the id of the team
     * @return the {@link Team} with the specified id
     */
    @Override
    public Team getTeamById(Long id){
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team "+id+" not found"));
    }

    /**
     * Retrieves a single team by its name
     *
     * @param name the name of the team
     * @return the {@link Team} with the specified name
     */
    @Override
    public Team findByName(String name){
        return teamRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Retrieves a single team by its country of origin
     *
     * @param country the teams country of origin
     * @return a list of {@link Team} entities from a specific country
     */
    @Override
    public List<Team> findByCountry(String country){
        return teamRepository.getByCountry(country);
    }

    /**
     * Retrieves all team in with a specific venue
     *
     * @param venueId the id of the team
     * @return a list of {@link Team} entities involving the specified team
     */
    @Override
    public List<Team> findByVenueId(Long venueId){
        return teamRepository.findByVenueId(venueId);
    }

    /**
     * Deletes a team by its id
     *
     * @param id the id of the team to delete
     */
    @Override
    public void deleteByid(Long id){
        teamRepository.deleteById(id);
    }

    /**
     * Creates a new team
     *
     * @param team the {@link Team} entity to create
     * @return the newly created {@link Team} entity
     */
    @Override
    public Team create(Team team){
        return teamRepository.save(team);
    }

    /**
     * Updates an existing team with new data
     *
     * @param team the {@link Team} entity with updated fields
     * @return the updated {@link Team} entity
     */
    @Override
    public Team  update(Team team){
        return teamRepository.save(team);
    }
}
