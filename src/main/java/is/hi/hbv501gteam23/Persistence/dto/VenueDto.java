package is.hi.hbv501gteam23.Persistence.dto;

public final class VenueDto {
    public record VenueRequest(
            String name,
            String address
    ) {}

    public record VenueResponse(
            Long id,
            String name,
            String address
    ) {}
}
