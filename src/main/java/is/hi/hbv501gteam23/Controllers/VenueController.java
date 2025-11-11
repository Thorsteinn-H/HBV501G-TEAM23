package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.dto.VenueDto;
import is.hi.hbv501gteam23.Services.Interfaces.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@Tag(name = "Venue", description = "Venue management")
@Controller
@RequestMapping("/venues")
@RequiredArgsConstructor
public class VenueController {
    private final VenueService venueService;

    /**
     * Retrieves all venues or filters them by optional id, name, or address.
     *
     * @param id      optional ID to filter by a single venue
     * @param name    optional name filter (case-insensitive, partial matches allowed)
     * @param address optional address filter (case-insensitive, partial matches allowed)
     * @return list of {@link VenueDto.VenueResponse} matching the criteria
     */
    @GetMapping
    @ResponseBody
    @Operation(
            summary = "Get all venues with optional filters",
            description = "Retrieve all venues or filter by optional parameters: id, name, or address."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venues successfully retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VenueDto.VenueResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid query parameters",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public List<VenueDto.VenueResponse> getVenues(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address
    ) {
        try {
            return venueService.findByFilters(id, name, address)
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
    @Operation(
            summary = "Get venue by ID",
            description = "Retrieve a single venue by its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venue successfully fetched"),
            @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    @ResponseBody
    public VenueDto.VenueResponse getVenueById(
            @Parameter(description = "ID of the venue to retrieve")@PathVariable Long id){
        return toResponse(venueService.findById(id));
    }

    /**
     * Creates a new venue.
     *
     * @param body venue creation request
     * @return the newly created {@link VenueDto.VenueResponse}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseBody
    @Operation(summary = "Create a new venue", description = "Requires ADMIN role")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Venue successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VenueDto.VenueResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Venue name already exists",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<VenueDto.VenueResponse> createVenue(@RequestBody VenueDto.VenueRequest body) {
        try {
            Venue created = venueService.createVenue(body);
            return ResponseEntity
                    .created(URI.create("/venues/" + created.getId()))
                    .body(toResponse(created));
        } catch (ResponseStatusException e) {
            throw e; // propagate specific errors (400, 409, etc.)
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
