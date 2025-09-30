package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Repositories.MatchRepository;
import is.hi.hbv501gteam23.Services.Interfaces.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Nafn : Þorsteinn H. Erlendsson
 * Tölvupóstur: the85@hi.is
 * Lýsing:
 **/

@Service
public class MatchServiceImplementation implements MatchService {
    private MatchRepository matchRepository;

    @Autowired
    public MatchServiceImplementation(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }
}
