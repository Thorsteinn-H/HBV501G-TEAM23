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
     *
     * @param id
     * @param name
     * @param address
     * @return
     */
    @Override
    public List<Venue> findByFilters(Long id, String name, String address) {
        Specification<Venue> spec = null;

        if (id != null) {
            spec = VenueSpecifications.hasId(id);
        }

        if (name != null && !name.isBlank()) {
            spec = (spec == null) ? VenueSpecifications.hasName(name) : spec.and(VenueSpecifications.hasName(name));
        }

        if (address != null && !address.isBlank()) {
            spec = (spec == null) ? VenueSpecifications.hasAddress(address) : spec.and(VenueSpecifications.hasAddress(address));
        }
        return venueRepository.findAll(spec);
    }

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
     * Creates a new venue
     *
     * @param body the {@link Venue} entity to create
     * @return the newly created {@link Venue} entity
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
