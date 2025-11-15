package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.dto.MetadataDto;

import java.util.List;

public interface MetadataService {

    /**
     *
     * @return
     */
    List<MetadataDto> getAllCountries();
}
