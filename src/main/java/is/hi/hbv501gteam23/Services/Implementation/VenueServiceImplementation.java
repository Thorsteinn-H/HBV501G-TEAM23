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
     * Retrieves all venue
     *
     * @return a list of all {@link Venue} entities
     */
    @Override
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    /**
     * Retrieves venues filtered by optional name and address.
     * <p>
     * Both filters are optional; when provided, they are combined with a logical AND.
     * If both {@code name} and {@code address} are {@code null} or blank,
     * all venues are returned.
     *
     * @param name    venue name filter (case-insensitive, partial matches allowed)
     * @param address venue address filter (case-insensitive, partial matches allowed)
     * @return a list of {@link Venue} entities matching the given filters
     */
    @Override
    public List<Venue> findByFilters(String name, String address) {
        Specification<Venue> spec = null;

        if (name != null && !name.isBlank()) {
            spec = VenueSpecifications.hasName(name);
        }

        if (address != null && !address.isBlank()) {
            spec = VenueSpecifications.hasAddress(address);
        }
        return venueRepository.findAll(spec);
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
     * Retrieves a single venue by its name.
     *
     * @param name the name of the venue
     * @return the {@link Venue} with the specified name
     * @throws ResponseStatusException with status 404 if the venue is not found
     */
    @Override
    public Venue findByName(String name) {
        Venue v = venueRepository.findByNameIgnoreCase(name);
        if (v == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue '" + name + "' not found");
        }
        return v;
    }

    /**
     * Creates a new venue.
     * <p>
     * Validates that the request body, name and address are present, and that
     * no other venue with the same name already exists.
     *
     * @param body the {@link VenueDto.VenueRequest} containing venue data to create
     * @return the newly created {@link Venue} entity
     *
     * @throws ResponseStatusException with status 400 if required fields are missing
     * @throws ResponseStatusException with status 409 if a venue with the same name already exists
     */
    @Override
    @Transactional
    public Venue createVenue(VenueDto.VenueRequest body) {
        if (body == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required");
        }

        String name = body.name() == null ? null : body.name().trim();
        String address = body.address() == null ? null : body.address().trim();

        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Venue name is required");
        }
        if (address == null || address.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Venue address is required");
        }

        Venue existing = venueRepository.findByNameIgnoreCase(name);
        if (existing != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Venue name already exists"); // 409
        }

        Venue v = new Venue();
        v.setName(name);
        v.setAddress(address);
        return venueRepository.save(v);
    }
}
