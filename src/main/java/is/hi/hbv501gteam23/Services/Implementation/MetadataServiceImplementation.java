package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.dto.MetadataDto;
import is.hi.hbv501gteam23.Services.Interfaces.MetadataService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * Service implementation for providing metadata used across the application,
 */
@Service
public class MetadataServiceImplementation implements MetadataService {
    /**
     * Retrieves all countries as ISO 3166-1 alpha-2 codes with their English display names.
     * <p>
     * The result is sorted alphabetically by country name (case-insensitive).
     *
     * @return a list of {@link MetadataDto} where {@code label} is the country name
     *         (in English) and {@code value} is the ISO country code
     */
    public List<MetadataDto> getAllCountries() {
        return Stream.of(Locale.getISOCountries())
            .map(code -> new Locale.Builder().setRegion(code).build())
            .map(locale -> new MetadataDto(locale.getDisplayCountry(Locale.ENGLISH), locale.getCountry()))
            .sorted((a, b) -> a.label().compareToIgnoreCase(b.label()))
            .toList();
    }
}