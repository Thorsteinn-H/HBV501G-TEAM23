package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.dto.CountryDto;
import is.hi.hbv501gteam23.Services.Interfaces.MetadataService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MetadataServiceImplementation implements MetadataService {
    public List<CountryDto> getAllCountries() {
        return Stream.of(Locale.getISOCountries())
            .map(code -> new Locale.Builder().setRegion(code).build())
            .map(locale -> new CountryDto(locale.getCountry(), locale.getDisplayCountry(Locale.ENGLISH)))
            .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
            .collect(Collectors.toList());
    }
}
