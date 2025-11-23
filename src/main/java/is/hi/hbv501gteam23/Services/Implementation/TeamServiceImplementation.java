package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Country;
import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.Repositories.CountryRepository;
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
    private final CountryRepository countryRepository;

    /**
     * Finds teams using optional filters, with sorting.
     * All filter parameters are optional; when {@code null}, they are ignored.
     *
     * @param filter the filters and sorting parameters for a team
     * @return list of {@link Team} entities matching the given filters
     */
    @Override
    public List<Team> listTeams(TeamDto.TeamFilter filter) {
        String name      = filter != null ? filter.name()      : null;
        Boolean isActive = filter != null ? filter.isActive()  : null;
        String country   = filter != null ? filter.country()   : null;
        String venueName = filter != null ? filter.venueName() : null;
        String sortBy  = filter != null ? filter.sortBy()  : null;
        String sortDir = filter != null ? filter.sortDir() : null;

        Specification<Team> spec= Specification.allOf(
                TeamSpecifications.nameContains(name),
                TeamSpecifications.hasActiveStatus(isActive),
                TeamSpecifications.hasCountry(country),
                TeamSpecifications.venueNameContains(venueName)
        );

        Sort sort = buildTeamSort(sortBy, sortDir);

        return teamRepository.findAll(spec,sort);
    }

    /**
     * Builds a {@link Sort} instance for team listing.
     * If no sortBy is provided, returns {@link Sort#unsorted()}.
     */
    private Sort buildTeamSort(String sortBy, String sortDir) {
        if (sortBy == null || sortBy.isBlank()) return Sort.unsorted();

        String property = switch (sortBy.trim()) {
            case "name"       -> "name";
            case "country"    -> "country";
            case "venueName"  -> "venue.name";
            default           -> "id";
        };

        Sort.Direction direction =
            (sortDir != null && sortDir.equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        if ("venue.name".equals(property)) return Sort.by(direction, "venue.name");
        return Sort.by(direction, property);
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
        if (body.name() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        if (body.country() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "country is required");

        if (teamRepository.findByNameContainingIgnoreCase(body.name()) != null)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Team with name already exists");

        Venue venue = null;
        if (body.venueId() != null) {
            venue = venueRepository.findById(body.venueId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue " + body.venueId() + " not found"));
        }

        String normalizedCode = MetadataUtils.normalizeCountryCode(body.country());
        Country country = countryRepository.findById(normalizedCode)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Country " + normalizedCode + " not found"));

        Team t = new Team();
        t.setName(body.name());
        t.setCountry(country);
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
        if (body.country() != null) {
            String normalizedCode = MetadataUtils.normalizeCountryCode(body.country());
            Country country = countryRepository.findById(normalizedCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Country " + normalizedCode + " not found"));
            t.setCountry(country);
        }
        if (body.isActive() != null) t.setActive(body.isActive());
        if (body.venueId() != null) {
            Venue v = venueRepository.findById(body.venueId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue " + body.venueId() + " not found"));
            t.setVenue(v);
        }

        return teamRepository.save(t);
    }
}
