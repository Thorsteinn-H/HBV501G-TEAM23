package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Persistence.Repositories.PlayerRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.TeamRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.VenueRepository;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto;
import is.hi.hbv501gteam23.Services.Interfaces.TeamService;
import is.hi.hbv501gteam23.Services.Interfaces.VenueService;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team "+id+" not found"));
    }

    /**
     * Retrieves a single team by its name
     *
     * @param name the name of the team
     * @return the {@link Team} with the specified name
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
     * Retrieves a single team by its country of origin
     *
     * @param country the teams country of origin
     * @return a list of {@link Team} entities from a specific country
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
     * Retrieves all teams in a specific venue
     *
     * @param venueId the id of the team
     * @return a list of {@link Team} entities involving the specified team
     */
    @Override
    public List<Team> findByVenueId(Long venueId) {
        if(venueRepository.existsById(venueId)){
            return teamRepository.findByVenueId(venueId);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue " + venueId + " not found");
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
        t.setCountry(body.country());
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team " + id + " not found"));

        if (body.name() != null)    t.setName(body.name());
        if (body.country() != null) t.setCountry(body.country());
        if (body.isActive() != null) t.setActive(body.isActive());
        if (body.venueId() != null) {
            Venue v = venueRepository.findById(body.venueId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue " + body.venueId() + " not found"));
            t.setVenue(v);
        }

        return teamRepository.save(t);
    }
}
