package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.dto.MetadataDto;
import is.hi.hbv501gteam23.Persistence.enums.Gender;
import is.hi.hbv501gteam23.Services.Interfaces.MetadataService;
import is.hi.hbv501gteam23.Utils.MetadataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "Metadata")
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
    @GetMapping("/countries")
    @Operation(
        summary = "List countries",
        description = "Fetches a list of all countries that can be assigned to a team or player."
    )
    public Map<String, List<Map<String, String>>> getAllCountries() {
        List<Map<String, String>> countries = metadataService.getAllCountries()
            .stream()
            .map(c -> new MetadataDto(c.label(), c.value()))
            .map(dto -> Map.of(
                "name", dto.label(),
                "code", dto.value()
            ))
            .toList();
        return Map.of("countries", countries);
    }

    @GetMapping("/genders")
    @Operation(summary = "List genders", description = "Returns all possible Gender enum values.")
    public Map<String, List<Map<String, String>>> getGenders() {
        List<Map<String, String>> genders = MetadataUtils.toMetadata(Gender.class, Gender::getLabel)
                .stream()
                .map(dto -> Map.of(
                        "label", dto.label(),
                        "value", dto.value()
                ))
                .toList();
        return Map.of("genders", genders);
    }
}
