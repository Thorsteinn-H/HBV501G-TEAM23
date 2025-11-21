package is.hi.hbv501gteam23.Persistence.dto;

public final class VenueDto {
    public record CreateVenueRequest(
            String name,
            String address
    ) {}

    public record PatchVenueRequest(
            String name,
            String address
    ) {}

    public record VenueResponse(
            Long id,
            String name,
            String address
    ) {}
}
