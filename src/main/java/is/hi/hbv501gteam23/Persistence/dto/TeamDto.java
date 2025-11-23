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
     * @param venueId the ID of the venue the team plays home games
     */
    public record CreateTeamRequest(
            String name,
            String country,
            Long venueId
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
     * Filter parameters for listing teams.
     *
     * All fields are optional; when {@code null} or blank, they are ignored.
     *
     * @param name      team name filter (contains, case-insensitive)
     * @param isActive  active status filter
     * @param country   country code filter (case-insensitive)
     * @param venueName venue name filter (contains, case-insensitive)
     * @param sortBy    sort field (e.g. "id", "name", "country", "venueName")
     * @param sortDir   sort direction, either {@code "ASC"} or {@code "DESC"}
     */
    public record TeamFilter(
            String name,
            Boolean isActive,
            String country,
            String venueName,
            String sortBy,
            String sortDir
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
            Boolean isActive,
            String country,
            Long venueId,
            String venueName
    ) {}
}
