package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.Repositories.PlayerRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.TeamRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.VenueRepository;
import is.hi.hbv501gteam23.Persistence.Specifications.TeamSpecifications;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto;
import is.hi.hbv501gteam23.Services.Interfaces.TeamService;
import is.hi.hbv501gteam23.Utils.MetadataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImplementation implements TeamService {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final VenueRepository venueRepository;

    /**
     * Finds teams using optional filters, with sorting.
     * All filter parameters are optional; when {@code null}, they are ignored.
     *
     * @param name      team name filter
     * @param isActive  active status filter
     * @param country   country code filter
     * @param venueName venue name filter
     * @param sortBy    field to sort by
     * @param sortDir   sort direction, either {@code "ASC"} or {@code "DESC"}
     * @return list of {@link Team} entities matching the given filters
     */
    @Override
    public List<Team> findTeamFilter(String name, Boolean isActive,
                                     String country, String venueName,
                                     String sortBy, String sortDir) {

        Specification<Team> spec= Specification.allOf(
                TeamSpecifications.teamName(name),
                TeamSpecifications.teamStatus(isActive),
                TeamSpecifications.teamCountry(country),
                TeamSpecifications.teamVenueName(venueName)
                );

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        return teamRepository.findAll(spec,sort);
    }

    /**
     * Retrieves a single team by its unique identifier.
     *
     * @param id the id of the team
     * @return the {@link Team} with the specified id
     * @throws ResponseStatusException with status 404 if the team is not found
     */
    @Override
    public Team getTeamById(Long id){
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team "+id+" not found"));
    }

    /**
     * Retrieves a single team by its name.
     *
     * @param name the name of the team
     * @return the {@link Team} with the specified name
     * @throws ResponseStatusException with status 404 if the team is not found
     */
    @Override
    public Team findByName(String name){
        Team t = teamRepository.findByNameContainingIgnoreCase(name);
        if (t == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team "+name+" not found");
        }
        return t;
    }

    /**
     * Retrieves teams by their country of origin.
     *
     * @param country the team's country of origin
     * @return a list of {@link Team} entities from the specified country
     * @throws ResponseStatusException with status 400 if the country is null or blank
     * @throws ResponseStatusException with status 404 if no teams are found for the country
     */
    @Override
    public List<Team> findByCountry(String country) {
        if (country == null || country.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "country is required");
        }
        String q = country.trim();
        List<Team> teams = teamRepository.findAllByCountryIgnoreCase(q);
        if (teams.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No teams found for country " + q);
        }
        return teams;
    }

    /**
     * Retrieves all teams that play at a specific venue.
     *
     * @param venueId the id of the venue
     * @return a list of {@link Team} entities associated with the specified venue
     * @throws ResponseStatusException with status 404 if the venue does not exist
     */
    @Override
    public List<Team> findByVenueId(Long venueId) {
        if(venueRepository.existsById(venueId)){
            return teamRepository.findByVenueId(venueId);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue " + venueId + " not found");
    }

    /**
     * Retrieves all teams with a specific active status.
     *
     * @param isActive the active status of a team
     * @return a list of teams with the same active status
     */
    @Override
    public List<Team> findByActiveStatus(Boolean isActive){
        return teamRepository.findByIsActive(isActive);
    }

    /**
     * Deletes a team by its id.
     * <p>
     * Before deletion, the team reference is cleared from all players belonging to the team.
     *
     * @param id the id of the team to delete
     */
    @Override
    @Transactional
    public void deleteTeam(Long id){
        playerRepository.clearTeamByTeamId(id);
        teamRepository.deleteById(id);
    }

    /**
     * Creates a new team from the given request body.
     * <p>
     * Validates that the name and country are provided, that the venue exists,
     * and that a team with the same name does not already exist.
     *
     * @param body the {@link TeamDto.CreateTeamRequest} containing team data
     * @return the newly created {@link Team} entity
     *
     * @throws ResponseStatusException with status 409 if a team with the same name already exists
     * @throws ResponseStatusException with status 404 if the venue is not found
     * @throws ResponseStatusException with status 400 if required fields are missing
     */
    @Override
    @Transactional
    public Team createTeam(TeamDto.CreateTeamRequest body) {
        Team existing = teamRepository.findByNameContainingIgnoreCase(body.name());
        if (existing != null) throw new ResponseStatusException(HttpStatus.CONFLICT, "Team with name already exists");

        Venue venue = venueRepository.findById(body.venueId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue " + body.venueId() + " not found"));


        Team t = new Team();
        if (body.name() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }
        t.setName(body.name());

        if (body.country() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "country is required");
        }
        t.setCountry(MetadataUtils.normalizeCountryCode(body.country()));
        t.setActive(true);
        t.setVenue(venue);

        return teamRepository.save(t);
    }

    /**
     * Partially updates a {@link Team} by id.
     * <p>
     * Applies only non-null fields from {@code body}. Supported fields:
     * {@code name}, {@code country}, {@code isActive} and {@code venueId}.
     * If {@code venueId} is present, it must reference an existing venue.
     *
     * @param id   the id of the team to update
     * @param body partial update payload for the team
     * @return the updated {@link Team}
     *
     * @throws ResponseStatusException with status 404 if the team or venue is not found
     */
    @Override
    @Transactional
    public Team patchTeam(Long id, TeamDto.PatchTeamRequest body) {
        Team t = teamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team " + id + " not found"));

        if (body.name() != null)    t.setName(body.name());
        if (body.country() != null) t.setCountry(MetadataUtils.normalizeCountryCode(body.country()));
        if (body.isActive() != null) t.setActive(body.isActive());
        if (body.venueId() != null) {
            Venue v = venueRepository.findById(body.venueId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue " + body.venueId() + " not found"));
            t.setVenue(v);
        }

        return teamRepository.save(t);
    }
}
