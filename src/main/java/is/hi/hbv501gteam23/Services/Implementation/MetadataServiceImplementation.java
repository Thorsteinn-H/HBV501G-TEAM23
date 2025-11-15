package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.dto.MetadataDto;
import is.hi.hbv501gteam23.Services.Interfaces.MetadataService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
public class MetadataServiceImplementation implements MetadataService {
    public List<MetadataDto> getAllCountries() {
        return Stream.of(Locale.getISOCountries())
            .map(code -> new Locale.Builder().setRegion(code).build())
            .map(locale -> new MetadataDto(locale.getDisplayCountry(Locale.ENGLISH), locale.getCountry()))
            .sorted((a, b) -> a.label().compareToIgnoreCase(b.label()))
            .toList();
    }
}
