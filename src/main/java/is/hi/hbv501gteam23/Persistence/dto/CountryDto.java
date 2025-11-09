package is.hi.hbv501gteam23.Persistence.dto;

/**
 * Response body for country
 * @param code area code for a country
 * @param name name of the country
 */
public record CountryDto(
        String code,
        String name
) {}
