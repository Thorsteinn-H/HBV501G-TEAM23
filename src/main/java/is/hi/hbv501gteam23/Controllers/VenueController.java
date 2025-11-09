package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto;
import is.hi.hbv501gteam23.Persistence.dto.VenueDto;
import is.hi.hbv501gteam23.Services.Interfaces.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/venues")
@RequiredArgsConstructor
public class VenueController {
    private final VenueService venueService;

    /**
     * Retrieves all {@link Venue} entities.
     * @return list of venue mapped to {@link VenueDto.VenueResponse}
     */
    @Operation(summary = "List all venues")
    @ApiResponse(responseCode = "200", description = "Venues successfully fetched")
    @GetMapping
    @ResponseBody
    public List<VenueDto.VenueResponse> getAllVenues(){
        return venueService.getAllVenues()
                .stream().map(this::toResponse).toList();
    }

    /**
     * Retrieves a {@link Venue} entity by id
     * @param id id of the venue to be retrieved
     * @return the team mapped to a {@link VenueDto.VenueResponse}
     */
    @GetMapping("/{id}")
    @ResponseBody
    public VenueDto.VenueResponse getVenueById(@PathVariable Long id){
        return toResponse(venueService.findById(id));
    }

    /**
     * Retrieves a {@link Venue} entity by name
     * @param name name of the venue to be retrieved
     * @return the team mapped to a {@link VenueDto.VenueResponse}
     */
    @GetMapping(params = "name")
    @ResponseBody
    public VenueDto.VenueResponse getVenueByName(@RequestParam String name) {
        return toResponse(venueService.findByName(name));
    }

    /**
     * Creates a new venue.
     * @param body the venue data to create
     * @return the created team mapped to {@link VenueDto.VenueResponse}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseBody
    public ResponseEntity<VenueDto.VenueResponse> createVenue(@RequestBody VenueDto.VenueRequest body) {
        Venue created = venueService.createVenue(body);
        return ResponseEntity
                .created(URI.create("/venues/" + created.getId()))
                .body(toResponse(created));
    }

    /**
     * Maps a {@link Venue} entity to a {@link VenueDto.VenueResponse} DTO.
     * @param v venue entity
     * @return mapped {@link VenueDto.VenueResponse}
     */
    private VenueDto.VenueResponse toResponse(Venue v) {
        return new VenueDto.VenueResponse(
                v.getId(),
                v.getName(),
                v.getAddress()
        );
    }
}
