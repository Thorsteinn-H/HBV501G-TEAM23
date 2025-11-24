package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.enums.FavoriteType;
import is.hi.hbv501gteam23.Persistence.enums.Gender;
import is.hi.hbv501gteam23.Persistence.enums.PlayerPosition;
import is.hi.hbv501gteam23.Persistence.enums.SystemRole;
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
        description = "Fetches a list of countries that can be assigned to a team or player."
    )
    public Map<String, List<Map<String, String>>> getAllCountries() {
        List<Map<String, String>> countries = metadataService.getAllCountries()
            .stream()
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

    @GetMapping("/positions")
    @Operation(summary = "List player positions", description = "Returns all possible PlayerPosition enum values.")
    public Map<String, List<Map<String, String>>> getPlayerPositions() {
        List<Map<String, String>> positions = MetadataUtils.toMetadata(PlayerPosition.class, PlayerPosition::getLabel)
            .stream()
            .map(dto -> Map.of(
                "label", dto.label(),
                "value", dto.value()
            ))
            .toList();
        return Map.of("positions", positions);
    }

    @GetMapping("/favorite-types")
    @Operation(summary = "List favorite types", description = "Returns all possible FavoriteType enum values.")
    public Map<String, List<Map<String, String>>> getFavoriteTypes() {
        List<Map<String, String>> types = MetadataUtils.toMetadata(FavoriteType.class, FavoriteType::getLabel)
            .stream()
            .map(dto -> Map.of(
                "label", dto.label(),
                "value", dto.value()
            ))
            .toList();
        return Map.of("favoriteTypes", types);
    }

    @GetMapping("/roles")
    @Operation(summary = "List system roles", description = "Returns all possible system roles.")
    public Map<String, List<Map<String, String>>> getSystemRoles() {
        List<Map<String, String>> roles = MetadataUtils.toMetadata(SystemRole.class, SystemRole::getLabel)
                .stream()
                .map(dto -> Map.of(
                        "label", dto.label(),
                        "value", dto.value()
                ))
                .toList();
        return Map.of("roles", roles);
    }
}
