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

/**
 * REST controller that exposes read/write operations for {@link Venue} resources.
 * <p>
 * Base path is /venues.
 */
@RestController
@RequestMapping("/venues")
@Tag(name = "Venue")
@RequiredArgsConstructor
public class VenueController {
    private final VenueService venueService;

    /**
     * Lists all venues with optional filters
     * * If {@code name} and/or {@code address} are provided, they are used to filter the results.
     *      * Matching is typically case-insensitive and may support partial matches,
     *      * depending on the {@link VenueService} implementation.
     *
     * @param name    name filter (case-insensitive, partial matches allowed)
     * @param address address filter (case-insensitive, partial matches allowed)
     * @return list of {@link VenueDto.VenueResponse} matching the criteria
     * @throws ResponseStatusException with status 500 if an unexpected error occurs while retrieving venues
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
     * Only admin is allowed
     *
     * @param body venue creation request
     * @return {@link ResponseEntity} with status 201 (CREATED) containing the newly created
     * {@link VenueDto.VenueResponse} and a {@code Location} header pointing to
     * /venues/{id}
     * @throws ResponseStatusException with status 500 if an unexpected error occurs while creating the venue
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
     * Maps a {@link Venue} entity to a {@link VenueDto.VenueResponse} DTO.
     *
     * @param v the venue entity to map
     * @return the mapped {@link VenueDto.VenueResponse}
     */
    private VenueDto.VenueResponse toResponse(Venue v) {
        return new VenueDto.VenueResponse(
                v.getId(),
                v.getName(),
                v.getAddress()
        );
    }
}
