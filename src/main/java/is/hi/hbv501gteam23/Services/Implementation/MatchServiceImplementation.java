package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.Repositories.MatchRepository;
import is.hi.hbv501gteam23.Services.Interfaces.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchServiceImplementation implements MatchService {
    private final MatchRepository matchRepository;

    @Autowired
    public MatchServiceImplementation(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public List<Match> listAll() {
        return matchRepository.findAll();
    }

    @Override
    public Match getById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found with id " + id));
    }
}
