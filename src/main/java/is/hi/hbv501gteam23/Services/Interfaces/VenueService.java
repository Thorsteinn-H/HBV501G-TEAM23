package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.dto.VenueDto;

import java.util.List;


public interface VenueService {

    /**
     * Finds venues using optional filters and sorting.
     * <p>
     * All fields in {@link VenueDto.VenueFilter} are optional; when {@code null} or blank, they are ignored.
     *
     * @param filter filter and sort parameters
     * @return list of {@link Venue} entities matching the given filters
     */
    List<Venue> listVenues(VenueDto.VenueFilter filter);

    /**
     * Retrieves a single venue by its unique identifier.
     *
     * @param id the id of the venue
     * @return the {@link Venue} with the specified id
     */
    Venue findById(Long id);

    /**
     * Creates a new venue
     *
     * @param body the {@link Venue} entity to create
     * @return the newly created {@link Venue} entity
     */
    Venue createVenue(VenueDto.CreateVenueRequest body);

    /**
     * Updates a venue
     *
     * @param id the id of the venue
     * @param body the {@link Venue} entity to update
     * @return the updated {@link Venue} entity
     */
    Venue updateVenue(Long id, VenueDto.PatchVenueRequest body);

    /**
     * Deletes a venue
     *
     * @param id the id of the venue
     */
    void deleteVenue(Long id);
}
