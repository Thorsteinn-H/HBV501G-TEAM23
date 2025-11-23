package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto;
import java.util.List;

public interface MatchService {

    /**
     * Finds matches using optional filters.
     *
     * @param filter filter parameters for listing matches
     * @return a list of {@link Match} entities matching the filters
     */
    List<Match> findMatchFilter(MatchDto.MatchFilter filter);

    /**
     * Retrieves a single match by its unique identifier.
     *
     * @param id the id of the match
     * @return the {@link Match} with the specified id
     */
    Match getMatchById(Long id);

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
