package is.hi.hbv501gteam23.Utils;

import is.hi.hbv501gteam23.Persistence.dto.MetadataDto;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class MetadataUtils {

    /**
     * Convert any enum to a list of MetadataDto
     *
     * @param enumClass the enum class
     * @param labelMapper function to get the label (usually enum.getLabel())
     * @param <E> enum type
     * @return List of MetadataDto
     */
    public static <E extends Enum<E>> List<MetadataDto> toMetadata(Class<E> enumClass, Function<E, String> labelMapper) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(e -> new MetadataDto(labelMapper.apply(e), e.name()))
                .toList();
    }

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
