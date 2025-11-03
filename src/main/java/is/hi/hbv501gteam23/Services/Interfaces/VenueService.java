package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto;
import is.hi.hbv501gteam23.Persistence.dto.VenueDto;

import java.util.List;


public interface VenueService {
    List<Venue> getAllVenues();
    Venue findById(Long id);
    Venue findByName(String name);
    Venue createVenue(VenueDto.VenueRequest body);
}
