package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.dto.CountryDto;

import java.util.List;

public interface MetadataService {
    List<CountryDto> getAllCountries();
}
