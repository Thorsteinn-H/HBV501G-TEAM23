package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.dto.CountryDto;
import is.hi.hbv501gteam23.Services.Interfaces.MetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Metadata", description = "Endpoints providing reference data")
@RestController
@RequestMapping("/metadata")
@RequiredArgsConstructor
public class MetadataController {
    private final MetadataService metadataService;

    /**
     * Returns a list of all countries with ISO 3166-1 alpha-2 codes.
     *
     * @return ResponseEntity containing the list of countries and HTTP status code 200.
     */
    @Operation(
        summary = "Retrieve all countries",
        description = "Fetches a list of all countries that can be assigned to a team or player. " +
            "Each country includes its ISO 3166-1 alpha-2 code and English display name."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Country list successfully fetched"),
        @ApiResponse(responseCode = "500", description = "Internal server error while fetching countries")
    })
    @GetMapping("/countries")
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        try {
            return ResponseEntity.ok(metadataService.getAllCountries());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
