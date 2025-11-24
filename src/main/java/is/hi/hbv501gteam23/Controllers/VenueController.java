package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.dto.VenueDto;
import is.hi.hbv501gteam23.Services.Interfaces.VenueService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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
     * Lists venues using optional filters, with sorting.
     * <p>
     * All fields in {@link VenueDto.VenueFilter} are optional; when {@code null} or blank, they are ignored.
     *
     * @param filter filter and sort parameters bound from query parameters
     * @return 200 OK with a list of {@link VenueDto.VenueResponse}
     */
    @GetMapping
    @Operation(summary = "List venues", description = "List all venues or filter by optional parameters. Supports sorting.")
    public ResponseEntity<List<VenueDto.VenueResponse>> listVenues(
        @ParameterObject @ModelAttribute VenueDto.VenueFilter filter
    ) {
        List<Venue> venues = venueService.listVenues(filter);
        List<VenueDto.VenueResponse> response = venues.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
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
    public ResponseEntity<VenueDto.VenueResponse> createVenue(@RequestBody VenueDto.CreateVenueRequest body) {
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
     * Updates a venue
     *
     * @param id the id of the venue
     * @param body the {@link Venue} entity to update
     * @return the updated {@link Venue} entity
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update venue", description = "Modify venue fields. Admin only.")
    public VenueDto.VenueResponse updateVenue(@PathVariable Long id, @RequestBody VenueDto.PatchVenueRequest body) {
        Venue updated = venueService.updateVenue(id, body);
        return toResponse(updated);
    }

    /**
     * Deletes a venue
     *
     * @param id the id of the venue
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete venue", description = "Delete a venue by ID. Admin only.")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
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
                v.getAddress(),
                v.getLatitude(),
                v.getLongitude()
        );
    }
}
