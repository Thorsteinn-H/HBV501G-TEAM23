package is.hi.hbv501gteam23.Persistence.dto;

public final class TeamDto {
    public record CreateTeamRequest(
            String name,
            String country,
            Long venueId
    ) {}

    public record TeamResponse(
            Long id,
            String name,
            String country,
            Long venueId,
            String venueName
    ) {}
}
