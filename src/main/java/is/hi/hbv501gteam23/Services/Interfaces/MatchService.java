package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Match;

import java.util.List;

public interface MatchService {
    List<Match> listAll();
    Match getById(Long id);
}
