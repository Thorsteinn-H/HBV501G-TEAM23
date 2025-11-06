package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.dto.VenueDto;
import is.hi.hbv501gteam23.Services.Interfaces.VenueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/venues")
@RequiredArgsConstructor
public class VenueController {
    private final VenueService venueService;

    @GetMapping
    @ResponseBody
    public List<VenueDto.VenueResponse> getAllTeams(){
        return venueService.getAllVenues()
                .stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public VenueDto.VenueResponse getVenueById(@PathVariable Long id){
        return toResponse(venueService.findById(id));
    }

    @GetMapping(params = "name")
    @ResponseBody
    public VenueDto.VenueResponse getVenueByName(@RequestParam String name) {
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
