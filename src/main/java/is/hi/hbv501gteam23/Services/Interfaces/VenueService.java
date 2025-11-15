package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.dto.VenueDto;

import java.util.List;


public interface VenueService {
    /**
     * Retrieves all venue
     *
     * @return a list of all {@link Venue} entities
     */
    List<Venue> getAllVenues();

    /**
     *
     * @param name
     * @param address
     * @return
     */
    List<Venue> findByFilters(String name, String address);

    /**
     * Retrieves a single venue by its unique identifier.
     *
     * @param id the id of the venue
     * @return the {@link Venue} with the specified id
     */
    Venue findById(Long id);

    /**
     * Retrieves a single venue by its name.
     *
     * @param name the name of the venue
     * @return the {@link Venue} with the specified name
     */
    Venue findByName(String name);

    /**
     * Creates a new venue
     *
     * @param body the {@link Venue} entity to create
     * @return the newly created {@link Venue} entity
     */
    Venue createVenue(VenueDto.VenueRequest body);
}
