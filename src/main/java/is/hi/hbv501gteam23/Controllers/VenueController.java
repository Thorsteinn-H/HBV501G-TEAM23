package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.dto.VenueDto;
import is.hi.hbv501gteam23.Services.Interfaces.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/venues")
@Tag(name = "Venue")
@RequiredArgsConstructor
public class VenueController {
    private final VenueService venueService;

    /**
     * Lists all venues with optional filters
     *
     * @param name    optional name filter (case-insensitive, partial matches allowed)
     * @param address optional address filter (case-insensitive, partial matches allowed)
     * @return list of {@link VenueDto.VenueResponse} matching the criteria
     */
    @GetMapping
    @ResponseBody
    @Operation(summary = "List venues", description = "List all venues or filter by optional parameters.")
    public List<VenueDto.VenueResponse> getVenues(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String address
    ) {
        try {
            return venueService.findByFilters(name, address)
                .stream()
                .map(this::toResponse)
                .toList();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving venues", e);
        }
    }

    /**
     * Retrieves a {@link Venue} entity by id
     * @param id id of the venue to be retrieved
     * @return the team mapped to a {@link VenueDto.VenueResponse}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get venue by ID", description = "Get a venue by its ID.")
    @ResponseBody
    public VenueDto.VenueResponse getVenueById(
            @Parameter @PathVariable Long id){
        return toResponse(venueService.findById(id));
    }

    /**
     * Creates a new venue.
     *
     * @param body venue creation request
     * @return the newly created {@link VenueDto.VenueResponse}
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    @Operation(summary = "Create venue", description = "Admin only.")
    public ResponseEntity<VenueDto.VenueResponse> createVenue(@RequestBody VenueDto.VenueRequest body) {
        try {
            Venue created = venueService.createVenue(body);
            return ResponseEntity
                    .created(URI.create("/venues/" + created.getId()))
                    .body(toResponse(created));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating venue", e);
        }
    }

    /**
     * Helper method to map a Venue entity to its response DTO.
     *
     * @param v the Venue entity
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
