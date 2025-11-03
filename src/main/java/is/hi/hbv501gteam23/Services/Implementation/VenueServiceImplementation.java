package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.Repositories.VenueRepository;
import is.hi.hbv501gteam23.Persistence.dto.VenueDto;
import is.hi.hbv501gteam23.Services.Interfaces.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VenueServiceImplementation implements VenueService {
    private final VenueRepository venueRepository;

    @Override
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    @Override
    public Venue findById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Venue " + id + " not found"));
    }

    @Override
    public Venue findByName(String name) {
        Venue v = venueRepository.findByNameIgnoreCase(name);
        if (v == null) {
            throw new jakarta.persistence.EntityNotFoundException("Venue '" + name + "' not found");
        }
        return v;
    }

    @Override
    @Transactional
    public Venue createVenue(VenueDto.VenueRequest body) {
        Venue existing = venueRepository.findByNameIgnoreCase(body.name());
        if (existing != null) {
            throw new IllegalArgumentException("Venue with name already exists");
        }

        Venue v = new Venue();
        v.setName(body.name());
        v.setAddress(body.address());
        return venueRepository.save(v);
    }
}
