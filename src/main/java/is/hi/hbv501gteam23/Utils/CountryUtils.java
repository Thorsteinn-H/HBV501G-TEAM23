package is.hi.hbv501gteam23.Utils;

import java.util.Arrays;
import java.util.Locale;

public class CountryUtils {

    /**
     * Normalizes and validates a 2-letter ISO country code.
     * @param code the country code to normalize.
     * @return uppercase code if valid, otherwise throws IllegalArgumentException.
     */
    public static String normalizeCountryCode(String code) {
        if (code == null || code.isBlank()) return null;
        String normalized = code.trim().toUpperCase(Locale.ROOT);

        boolean isValid = Arrays.asList(Locale.getISOCountries()).contains(normalized);
        if (!isValid) {
            throw new IllegalArgumentException("Invalid ISO country code: " + code
                    + ". Refer to /metadata/countries for valid country codes.");
        }

        return normalized;
    }

    /**
     * Finds the country name based on a given country code
     * @param code a 2-letter ISO country code.
     * @return the country name.
     */
    public static String getCountryName(String code) {
        if (code == null) return null;
        Locale locale = new Locale.Builder().setRegion(code).build();
        return locale.getDisplayCountry(Locale.ENGLISH);
    }
}
