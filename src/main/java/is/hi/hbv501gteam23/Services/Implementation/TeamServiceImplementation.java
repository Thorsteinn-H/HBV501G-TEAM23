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

    @Override
    public List<Team> getAllTeams(){
        return teamRepository.findAll();
    }

    @Override
    public Team getTeamById(Long id){
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team "+id+" not found"));
    }

    @Override
    public Team findByName(String name){
        return teamRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Team> findByCountry(String country){
        return teamRepository.getByCountry(country);
    }

    @Override
    public List<Team> findByVenueId(Long venueId){
        return teamRepository.findByVenueId(venueId);
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
