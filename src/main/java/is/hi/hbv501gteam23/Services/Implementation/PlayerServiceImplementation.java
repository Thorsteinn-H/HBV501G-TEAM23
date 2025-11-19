package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Country;
import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.Repositories.CountryRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.PlayerRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.TeamRepository;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto;
import is.hi.hbv501gteam23.Services.Interfaces.PlayerService;
import is.hi.hbv501gteam23.Utils.MetadataUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service for handling logic related to players
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PlayerServiceImplementation implements PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final CountryRepository countryRepository;

    /**
     *
     * @param name
     * @param teamId
     * @param teamName
     * @param countryCode
     * @param isActive
     * @param sortBy
     * @param sortDir
     * @param page
     * @param size
     * @return
     */
    @Override
    public List<Player> findPlayers(
            String name,
            Long teamId,
            String teamName,
            String countryCode,
            Boolean isActive,
            String sortBy,
            String sortDir,
            int page,
            int size
    ) {
        Specification<Player> spec = (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (name != null && !name.isBlank()) {
                p = cb.and(p, cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (teamId != null) {
                p = cb.and(p, cb.equal(root.get("team").get("id"), teamId));
            }

            if (teamName != null && !teamName.isBlank()) {
                p = cb.and(p, cb.like(cb.lower(root.get("team").get("name")), "%" + teamName.toLowerCase() + "%"));
            }

            if (countryCode != null && !countryCode.isBlank()) {
                p = cb.and(p, cb.equal(root.get("country").get("code"), countryCode.toUpperCase()));
            }

            if (isActive != null) {
                p = cb.and(p, cb.equal(root.get("active"), isActive));
            }

            return p;
        };

        Sort.Direction direction = "DESC".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return playerRepository.findAll(spec, pageable).getContent();
    }

    /**
     * Retrieves all players
     *
     * @return a list of all {@link Player} entities
     */
    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    /**
     * Retrieves a single player by its unique identifier.
     *
     * @param id the ID of the match
     * @return the {@link Player} with the specified id
     */
    @Override
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player "+id+" not found"));
    }

    /**
     * Retrieves a single player by its name
     *
     * @param name the name of the player
     * @return the {@link Player} with the specified name
     */
    @Override
    public Player searchPlayersByName(String name) {
        Player p = playerRepository.findByNameContainingIgnoreCase(name);
        if (p == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player "+name+" not found");
        }
        return p;
    }

    /**
     * Retrieves a list of players by its team name
     *
     * @param teamName the ID of the match
     * @return a list of all {@link Player} entities with the specified team name
     */
    @Override
    public List<Player> getByTeamName(String teamName) {
        List<Player> nameOfTeam = playerRepository.findByTeam_NameIgnoreCase(teamName);
        if (nameOfTeam == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No teams with name "+teamName+" found");
        }
        return nameOfTeam;
    }

    /**
     * Retrieves a list of players by its team unique identifier.
     *
     * @param teamId the ID of the match
     * @return a list of all {@link Player} entities with the specified team id
     */
    @Override
    public List<Player> getByTeamId(Long teamId) {
        if (teamRepository.existsById(teamId)) {
            return playerRepository.findByTeamId(teamId);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team "+teamId+" not found");
    }

    /**
     * Retrieves a list of active players
     *
     * @param isActive the active status of the player
     * @return a list of all {@link Player} entities with active status
     */
    @Override
    public List<Player> getActivePlayers(Boolean isActive) {
        return playerRepository.findByIsActive(isActive);
    }

    /**
     *
     * @param country the country to filter by
     * @return a list of all {@link Player} entities from the country
     */
    @Override
    public List<Player> findByPlayerCountry(Country country) {
        return playerRepository.findByCountry(country);
    }


    /**
     * Creates a new player
     *
     * @param body the {@link Player} entity to create
     * @return the newly created {@link Player} entity
     */
    @Override
    @Transactional
    public Player createPlayer(PlayerDto.CreatePlayerRequest body) {
        if (body == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required");

        String name = body.name() == null ? null : body.name().trim();
        if (name == null || name.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player name is required");
        if (body.dateOfBirth() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dateOfBirth is required");
        if (body.dateOfBirth().isAfter(java.time.LocalDate.now())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dateOfBirth cannot be in the future");
        if (body.goals() != null && body.goals() < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "goals must be >= 0");
        if (body.position() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "position is required");

        if (playerRepository.findByNameContainingIgnoreCase(name) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Player " + name + " already exists");
        }

        Team team = null;
        if (body.teamId() != null) {
            team = teamRepository.findById(body.teamId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team " + body.teamId() + " not found"));
        }

        String normalizedCode = MetadataUtils.normalizeCountryCode(body.country());
        Country country = countryRepository.findById(normalizedCode)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Country " + normalizedCode + " not found"));

        Player p = new Player();
        p.setName(name);
        p.setDateOfBirth(body.dateOfBirth());
        p.setCountry(country);
        p.setPosition(body.position());
        p.setGoals(body.goals() != null ? body.goals() : 0);
        p.setTeam(team);

        return playerRepository.save(p);
    }


    /**
     * Partially updates a {@link Player} by id.
     * Applies only non-null fields from {@code body}. Supported fields:
     * {@code name}, {@code dateOfBirth}, {@code country}, {@code position},
     * {@code goals}, {@code teamId}. If {@code teamId} is present, it must
     * reference an existing team.
     *
     * @param id   the id of the player to update
     * @param body partial update payload for the player
     * @return the updated {@link Player}
     *
     * @throws jakarta.persistence.EntityNotFoundException
     *         if the player does not exist, or if a provided {@code teamId} cannot be found
     */
    @Override
    @Transactional
    public Player patchPlayer(Long id, PlayerDto.PatchPlayerRequest body) {
        Player p = playerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Player " + id + " not found"));

        if (body.name() != null)        p.setName(body.name());
        if (body.dateOfBirth() != null) p.setDateOfBirth(body.dateOfBirth());
        if (body.country() != null) {
            String normalizedCode = MetadataUtils.normalizeCountryCode(body.country());
            Country country = countryRepository.findById(normalizedCode)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Country " + normalizedCode + " not found"));
            p.setCountry(country);
        }
        if (body.position() != null)    p.setPosition(body.position());
        if (body.goals() != null)       p.setGoals(body.goals());
        if (body.isActive() != null)    p.setActive(body.isActive());
        if (body.teamId() != null) {
            Team team = teamRepository.findById(body.teamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team " + body.teamId() + " not found"));
            p.setTeam(team);
        }
        return playerRepository.save(p);
    }

    /**
     * Deletes a player by its id
     *
     * @param id the id of the player to delete
     */
    @Override
    public void deletePlayer(Long id) {
        if (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player " + id + " not found");
        }
    }
}
