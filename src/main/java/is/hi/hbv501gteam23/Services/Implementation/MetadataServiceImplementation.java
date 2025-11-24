package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Repositories.CountryRepository;
import is.hi.hbv501gteam23.Persistence.dto.MetadataDto;
import is.hi.hbv501gteam23.Services.Interfaces.MetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service implementation for providing metadata used across the application,
 */
@Service
@RequiredArgsConstructor
public class MetadataServiceImplementation implements MetadataService {
    private final CountryRepository countryRepository;

    /**
     * Retrieves all countries as ISO 3166-1 alpha-2 codes with their English display names.
     * <p>
     * The result is sorted alphabetically by country name (case-insensitive).
     *
     * @return a list of {@link MetadataDto} where {@code label} is the country name
     *         (in English) and {@code value} is the ISO country code
     */
    @Override
    public List<MetadataDto> getAllCountries() {
        return countryRepository.findAll()
            .stream()
            .map(c -> new MetadataDto(c.getCountryName(), c.getCode()))
            .sorted((a, b) -> a.label().compareToIgnoreCase(b.label()))
            .toList();
    }
}
