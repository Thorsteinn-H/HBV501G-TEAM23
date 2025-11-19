package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto;

import java.time.LocalDate;
import java.util.List;

public interface MatchService {

    /**
     * Finds matches using optional filters.
     * <p>
     * All parameters are optional; when {@code null}, the corresponding filter
     * is ignored. Results can be sorted by a given field and direction.
     *
     * @param startDate    lower bound (inclusive) for the match date
     * @param endDate      upper bound (inclusive) for the match date
     * @param homeGoals    exact number of goals scored by the home team
     * @param awayGoals    exact number of goals scored by the away team
     * @param homeTeamName home team name filter
     * @param awayTeamName away team name filter
     * @param venueName    venue name filter
     * @param sortBy       field to sort by
     * @param sortDir      sort direction, either {@code "ASC"} or {@code "DESC"}
     * @return a list of {@link Match} entities matching the filters
     */
    List<Match> findMatchFilter(
            LocalDate startDate,LocalDate endDate,Integer homeGoals,Integer
                    awayGoals,String homeTeamName,String
                    awayTeamName, String venueName, String sortBy, String sortDir);
    /**
     * Retrieves a single match by its unique identifier.
     *
     * @param id the id of the match
     * @return the {@link Match} with the specified id
     */
    Match getMatchById(Long id);


    /**
     * Retrieves all matches in which a specific team has participated.
     *
     * @param teamId the ID of the team
     * @return a list of {@link Match} entities involving the specified team
     */
    List<Match> getMatchesByTeamId(Long teamId);

    /**
     * Retrieves all matches in a specific time frame
     * @param from start of the time fram
     * @param to end of the time framm
     * @return a list of {@link Match} entities that happened in the time frame
     */
    List<Match> getMatchesBetween(LocalDate from, LocalDate to);


    /**
     * Updates existing match
     * @param id the id of the match to update
     * @param body partial update payload for the match
     * @return 200 OK with the updated {@link MatchDto.MatchResponse};
     */
    Match patchMatch(Long id, MatchDto.PatchMatchRequest body);

    /**
     * Creates a match
     * @param body the {@link Match} entity to create
     * @return the created match
     */
    Match createMatch(MatchDto.CreateMatchRequest body);


    /**
     * Deletes a match by its id
     *
     * @param id the id of the match to delete
     */
    void deleteMatch(Long id);
}
