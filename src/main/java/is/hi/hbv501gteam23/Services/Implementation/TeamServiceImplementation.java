package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.Repositories.PlayerRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.TeamRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.VenueRepository;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto;
import is.hi.hbv501gteam23.Services.Interfaces.TeamService;
import is.hi.hbv501gteam23.Services.Interfaces.VenueService;
import is.hi.hbv501gteam23.Utils.CountryUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImplementation implements TeamService {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final VenueRepository venueRepository;
    private final VenueService venueService;


    /**
     * Retrieves all teams
     *
     * @return a list of all {@link Team} entities
     */
    @Override
    public List<Team> getAllTeams(){
        return teamRepository.findAll();
    }

    /**
     * Retrieves a single team by its unique identifier.
     *
     * @param id the id of the team
     * @return the {@link Team} with the specified id
     */
    @Override
    public Team getTeamById(Long id){
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team "+id+" not found"));
    }

    /**
     * Retrieves a single team by its name
     *
     * @param name the name of the team
     * @return the {@link Team} with the specified name
     */
    @Override
    public Team findByName(String name){
        return teamRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Retrieves a single team by its country of origin
     *
     * @param country the teams country of origin
     * @return a list of {@link Team} entities from a specific country
     */
    @Override
    public List<Team> findByCountry(String country){
        return teamRepository.findByCountry(country);
    }

    /**
     * Retrieves all teams in a specific venue
     *
     * @param venueId the id of the team
     * @return a list of {@link Team} entities involving the specified team
     */
    @Override
    public List<Team> findByVenueId(Long venueId){
        return teamRepository.findByVenueId(venueId);
    }

    /**
     * Retrieves all teams with a specific active status
     *
     * @param isActive the active status of a team
     * @return a list of teams with the same active status
     */
    @Override
    public List<Team> findByActiveStatus(Boolean isActive){
        return teamRepository.findByIsActive(isActive);
    }

    /**
     * Deletes a team by its id
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
     * Creates a new team
     *
     * @param body the {@link Team} entity to create
     * @return the newly created {@link Team} entity
     */
    @Override
    @Transactional
    public Team createTeam(TeamDto.CreateTeamRequest body) {
        Team existing = teamRepository.findByNameContainingIgnoreCase(body.name());
        if (existing != null) throw new IllegalArgumentException("Team with name already exists");

        Venue venue = venueRepository.findById(body.venueId())
                .orElseThrow(() -> new EntityNotFoundException("Venue " + body.venueId() + " not found"));

        Team t = new Team();
        t.setName(body.name());
        t.setCountry(CountryUtils.normalizeCountryCode(body.country()));
        t.setActive(true);
        t.setVenue(venue);

        return teamRepository.save(t);
    }

    /**
     * Partially updates a {@link Team} by id.
     * Applies only non-null fields from {@code body}. Supported fields:
     * {@code name}, {@code country}, and {@code venueId}. If {@code venueId} is present,
     * it must reference an existing venue.
     *
     * @param id   the id of the team to update
     * @param body partial update payload for the team
     * @return the updated {@link Team}
     *
     * @throws jakarta.persistence.EntityNotFoundException
     *         if the team does not exist, or if a provided {@code venueId} cannot be found
     */
    @Override
    @Transactional
    public Team patchTeam(Long id, TeamDto.PatchTeamRequest body) {
        Team t = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team " + id + " not found"));

        if (body.name() != null)    t.setName(body.name());
        if (body.country() != null) t.setCountry(CountryUtils.normalizeCountryCode(body.country()));
        if (body.isActive() != null) t.setActive(body.isActive());
        if (body.venueId() != null) {
            Venue v = venueRepository.findById(body.venueId())
                    .orElseThrow(() -> new EntityNotFoundException("Venue " + body.venueId() + " not found"));
            t.setVenue(v);
        }

        return teamRepository.save(t);
    }
}
