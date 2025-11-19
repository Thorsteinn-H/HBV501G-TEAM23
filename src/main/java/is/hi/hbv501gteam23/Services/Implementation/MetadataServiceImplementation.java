package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Repositories.CountryRepository;
import is.hi.hbv501gteam23.Persistence.dto.MetadataDto;
import is.hi.hbv501gteam23.Services.Interfaces.MetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetadataServiceImplementation implements MetadataService {
    private final CountryRepository countryRepository;

    @Override
    public List<MetadataDto> getAllCountries() {
        return countryRepository.findAll()
            .stream()
            .map(c -> new MetadataDto(c.getCountryName(), c.getCode()))
            .sorted((a, b) -> a.label().compareToIgnoreCase(b.label()))
            .toList();
    }
}
