package is.hi.hbv501gteam23.Persistence.dto;

public final class TeamDto {
    /**
     * Request body for creating a new team
     *
     * <p>
     *     This DTO is used in POST request to create a team with a name, country and venue
     * </p>
     *
     * @param name name of a team
     * @param country a teams country of origin
     * @param venueId the ID of the venue the team plays home games
     */
    public record CreateTeamRequest(
            String name,
            String country,
            Long venueId
    ) {}

    /**
     * Request body for retrieving a team
     *
     * <p>
     *     This DTO is used in GET request to get a team with an id, name, country, venueID and venueName
     * </p>
     * DTO representing a team returned by the API.
     * @param id ID of a team
     * @param name name of a team
     * @param country a teams country of origin
     * @param venueId the ID of the venue the team plays home games
     * @param venueName the name of the venue the team plays home games
     */
    public record TeamResponse(
            Long id,
            String name,
            String country,
            Long venueId,
            String venueName
    ) {}
}
