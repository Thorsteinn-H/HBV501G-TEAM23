package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing and managing {@link Match} entities.
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> , JpaSpecificationExecutor<Match> {
}
