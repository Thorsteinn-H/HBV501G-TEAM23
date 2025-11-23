package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.Repositories.VenueRepository;
import is.hi.hbv501gteam23.Persistence.Specifications.VenueSpecifications;
import is.hi.hbv501gteam23.Persistence.dto.VenueDto;
import is.hi.hbv501gteam23.Services.Interfaces.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Sort;
import java.util.List;

/**
 * Service implementation for handling business logic related to {@link Venue} entities.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VenueServiceImplementation implements VenueService {
    private final VenueRepository venueRepository;

    /**
     * Finds venues using optional filters and sorting.
     * <p>
     * All fields in {@link VenueDto.VenueFilter} are optional; when {@code null} or blank, they are ignored.
     *
     * @param filter filter and sort parameters
     * @return list of {@link Venue} entities matching the given filters
     */
    @Override
    public List<Venue> listVenues(VenueDto.VenueFilter filter) {
        String name    = filter != null ? filter.name()    : null;
        String address = filter != null ? filter.address() : null;
        String sortBy = filter != null ? filter.sortBy() : null;
        String sortDir = filter != null ? filter.sortDir() : null;

        Specification<Venue> spec = Specification.allOf(
                VenueSpecifications.nameContains(name),
                VenueSpecifications.addressContains(address)
        );
        Sort sort = buildVenueSort(sortBy, sortDir);
        return venueRepository.findAll(spec, sort);
    }

    /**
     * Builds a {@link Sort} instance for venue listing.
     *
     * @param sortBy  requested sort field
     * @param sortDir requested sort direction (ASC/DESC)
     * @return a {@link Sort} configured for the requested field and direction
     */
    private Sort buildVenueSort(String sortBy, String sortDir) {
        String key = sortBy == null ? "" : sortBy.trim();

        String property = switch (key) {
            case "name"       -> "name";
            case "address"    -> "address";
            default           -> "id";
        };

        Sort.Direction direction;
        if (sortDir == null) direction = Sort.Direction.ASC;
        else if (sortDir.equalsIgnoreCase("desc")) direction = Sort.Direction.DESC;
        else direction = Sort.Direction.ASC;

        return Sort.by(direction, property);
    }

    /**
     * Retrieves a single venue by its ID.
     *
     * @param id the id of the venue
     * @return the {@link Venue} with the specified id
     * @throws ResponseStatusException with status 404 if the venue is not found
     */
    @Override
    public Venue findById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue " + id + " not found"));
    }

    /**
     * Creates a new venue.
     * <p>
     * Validates that the request body, name and address are present, and that
     * no other venue with the same name already exists.
     *
     * @param body the {@link VenueDto.CreateVenueRequest} containing venue data to create
     * @return the newly created {@link Venue} entity
     *
     * @throws ResponseStatusException with status 400 if required fields are missing
     * @throws ResponseStatusException with status 409 if a venue with the same name already exists
     */
    @Override
    @Transactional
    public Venue createVenue(VenueDto.CreateVenueRequest body) {
        if (body == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required");

        String name = body.name() == null ? null : body.name().trim();
        String address = body.address() == null ? null : body.address().trim();

        if (name == null || name.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Venue name is required");
        if (address == null || address.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Venue address is required");

        Venue existing = venueRepository.findByNameIgnoreCase(name);
        if (existing != null) throw new ResponseStatusException(HttpStatus.CONFLICT, "Venue name already exists"); // 409

        Venue v = new Venue();
        v.setName(name);
        v.setAddress(address);
        return venueRepository.save(v);
    }

    /**
     * Updates a venue's name or address
     *
     * @param id the id of the venue
     * @param body the {@link Venue} entity to update
     * @return the updated {@link Venue} entity
     */
    @Override
    @Transactional
    public Venue updateVenue(Long id, VenueDto.PatchVenueRequest body) {
        Venue venue = venueRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Venue " + id + " not found"
            ));

        if (body == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required");

        String newName = body.name() == null ? null : body.name().trim();
        String newAddress = body.address() == null ? null : body.address().trim();

        if (newName != null && !newName.isBlank()) {
            Venue existing = venueRepository.findByNameIgnoreCase(newName);
            if (existing != null && !existing.getId().equals(id)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Venue name already exists");
            venue.setName(newName);
        }

        if (newAddress != null && !newAddress.isBlank()) venue.setAddress(newAddress);

        return venueRepository.save(venue);
    }

    /**
     * Deletes a venue
     *
     * @param id the id of the venue
     */
    @Override
    @Transactional
    public void deleteVenue(Long id) {
        Venue venue = venueRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Venue " + id + " not found"
            ));
        venueRepository.delete(venue);
    }
}
