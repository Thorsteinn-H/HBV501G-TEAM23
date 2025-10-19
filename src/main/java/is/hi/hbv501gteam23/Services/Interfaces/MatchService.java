package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Match;

import java.util.List;

public interface MatchService {
    /**
     * Retrieves all matches
     *
     * @return a list of all {@link Match} entities
     */
    List<Match> getAllMatches();

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
     * Retrieves all matches were played during a specific year.
     *
     * @param year the year to filter matches
     * @return a list of {@link Match} entities played during the given year
     */
    List<Match> getMatchesByYear(int year);


    /**
     * Updates an existing match with new data
     *
     * @param match the {@link Match} entity with updated fields
     * @return the updated {@link Match} entity
     */
    Match updateMatch(Match match);


    /**
     * Creates a new match
     *
     * @param match the {@link Match} entity to create
     * @return the newly created {@link Match} entity
     */
    Match createMatch(Match match);


    /**
     * Deletes a match by its id
     *
     * @param id the id of the match to delete
     */
    void deleteMatch(Long id);
}
