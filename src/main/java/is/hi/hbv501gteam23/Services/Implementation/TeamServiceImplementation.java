package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.Repositories.TeamRepository;
import is.hi.hbv501gteam23.Services.Interfaces.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImplementation implements TeamService {
    private TeamRepository teamRepository;

    @Autowired
    public TeamServiceImplementation(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Team findById(long id){
        return teamRepository.findById(id);
    }

    @Override
    public Team findByName(String name){
        return teamRepository.findByName(name);
    }

    @Override
    public Team findByCountry(String country){
        return teamRepository.findByCountry(country);
    }

    @Override
    public List<Team> findByVenueId(Long venueId){
        return teamRepository.findByVenueId(venueId);
    }

    @Override
    public List<Team> findAll(){
        return teamRepository.findAll();
    }

    @Override
    public void deleteByid(Long id){
        teamRepository.deleteById(id);
    }

    @Override
    public Team create(Team team){
        return teamRepository.save(team);
    }

    @Override
    public Team  update(Team team){
        return teamRepository.save(team);
    }
}
