package is.hi.hbv501gteam23.Persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

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
     * @param venueName the ID of the venue the team plays home games
     */
    public record CreateTeamRequest(
            String name,
            String country,
            Long venueName
    ) {}

    /**
     * Updating body for Team
     * All fields are optional; only non-null values will be applied.
     *      Typical uses include renaming a team, changing the country,
     *      or reassigning the team to a different venue.
     * @param name     new team name (optional)
     * @param isActive new active status (optional)
     * @param country  new team country (optional)
     * @param venueId  id of the target venue (optional; must reference an existing venue)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record PatchTeamRequest(
            String name,
            Boolean isActive,
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
     * @param isActive active status of a team
     * @param country a teams country of origin
     * @param venueId the ID of the venue the team plays home games
     * @param venueName the name of the venue the team plays home games
     */
    public record TeamResponse(
            Long id,
            String name,
            boolean isActive,
            String country,
            Long venueId,
            String venueName
    ) {}
}
