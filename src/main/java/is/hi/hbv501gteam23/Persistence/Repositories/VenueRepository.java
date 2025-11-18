package is.hi.hbv501gteam23.Persistence.Repositories;

import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VenueRepository extends JpaRepository<Venue, Long>, JpaSpecificationExecutor<Venue> {
    /**
     * Finds a venue by its name, ignoring case.
     *
     * @param name the venue name to search for
     * @return the matching {@link Venue}, or {@code null} if none is found
     */
    Venue findByNameIgnoreCase(String name);
}
