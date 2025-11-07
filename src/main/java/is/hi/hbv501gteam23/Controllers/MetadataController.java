package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import is.hi.hbv501gteam23.Persistence.dto.CountryDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/metadata")
public class MetadataController {

    /**
     * Lists all countries that can be assigned to a team or player
     * @return a list of all countries with their ISO 3166-1 alpha-2 country codes.
     */
    @Operation(summary = "List all countries")
    @ApiResponse(responseCode = "200", description = "Country list successfully fetched")
    @GetMapping("/countries")
    public List<CountryDto> getAllCountries() {
        return Stream.of(Locale.getISOCountries())
                .map(code -> new Locale.Builder().setRegion(code).build())
                .map(locale -> new CountryDto(locale.getCountry(), locale.getDisplayCountry(Locale.ENGLISH)))
                .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
                .collect(Collectors.toList());
    }
}
