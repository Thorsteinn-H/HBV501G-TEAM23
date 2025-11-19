package is.hi.hbv501gteam23.Config;

import is.hi.hbv501gteam23.Persistence.Entities.Country;
import is.hi.hbv501gteam23.Persistence.Repositories.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountrySyncService {

    private final CountryRepository countryRepository;

    /**
     * Synchronize countries from Java's Locale with the database in bulk.
     */
    @Transactional
    public void syncCountriesBulk() {
        Map<String, String> javaCountries = Arrays.stream(Locale.getISOCountries())
                .collect(Collectors.toMap(
                        iso -> iso,
                        iso -> new Locale.Builder().setRegion(iso).build().getDisplayCountry(Locale.ENGLISH)
                ));

        Map<String, Country> dbCountries = countryRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Country::getCode, c -> c));

        List<Country> toInsert = new ArrayList<>();
        List<Country> toUpdate = new ArrayList<>();

        for (var entry : javaCountries.entrySet()) {
            String iso = entry.getKey();
            String name = entry.getValue();

            if (!dbCountries.containsKey(iso)) {
                toInsert.add(new Country(iso, name));
            } else {
                Country c = dbCountries.get(iso);
                if (!name.equals(c.getCountryName())) {
                    c.setCountryName(name);
                    toUpdate.add(c);
                }
            }
        }

        if (!toInsert.isEmpty()) countryRepository.saveAll(toInsert);
        if (!toUpdate.isEmpty()) countryRepository.saveAll(toUpdate);
    }
}
