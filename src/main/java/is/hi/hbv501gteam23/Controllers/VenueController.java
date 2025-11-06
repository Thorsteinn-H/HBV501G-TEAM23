package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.dto.VenueDto;
import is.hi.hbv501gteam23.Services.Interfaces.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/venues")
@RequiredArgsConstructor
public class VenueController {
    private final VenueService venueService;

    @GetMapping
    public List<VenueDto.VenueResponse> getAllTeams(){
        return venueService.getAllVenues()
                .stream().map(this::toResponse).toList();
    }

    @GetMapping("/name={name}")
    public VenueDto.VenueResponse getVenueByName(@PathVariable("name") String name) {
        return toResponse(venueService.findByName(name));
    }

    private VenueDto.VenueResponse toResponse(Venue v) {
        return new VenueDto.VenueResponse(
                v.getId(),
                v.getName(),
                v.getAddress()
        );
    }
}
