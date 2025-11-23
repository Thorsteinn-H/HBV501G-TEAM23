package is.hi.hbv501gteam23.Persistence.dto;

import java.math.BigDecimal;

public final class VenueDto {
    public record CreateVenueRequest(
            String name,
            String address
    ) {}

    public record PatchVenueRequest(
            String name,
            String address
    ) {}

    /**
     * Filter parameters for listing venues.
     * All fields are optional; when {@code null} or blank, they are ignored.
     *
     * @param name    venue name to filter by (contains, case-insensitive)
     * @param address venue address to filter by (contains, case-insensitive)
     * @param sortBy  sort field (e.g. "id", "name", "address")
     * @param sortDir sort direction, either {@code "ASC"} or {@code "DESC"}
     */
    public record VenueFilter(
            String name,
            String address,
            String sortBy,
            String sortDir
    ) {}

    public record VenueResponse(
            Long id,
            String name,
            String address,
            BigDecimal latitude,
            BigDecimal longitude
    ) {}
}
