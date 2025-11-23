package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Country;
import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.Repositories.CountryRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.PlayerRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.TeamRepository;
import is.hi.hbv501gteam23.Persistence.Specifications.PlayerSpecifications;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto;
import is.hi.hbv501gteam23.Services.Interfaces.PlayerService;
import is.hi.hbv501gteam23.Utils.MetadataUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
     * Finds players using optional filters, with sorting and pagination.
     * All filter parameters are optional; when {@code null} or blank, they are ignored.
     *
     * @param filter filter and sort parameters
     * @return list of {@link Player} entities matching the given filters
     */
    @Override
    public List<Player> findPlayers(PlayerDto.PlayerFilter filter) {
        String name = filter != null ? filter.name() : null;
        Long teamId = filter != null ? filter.teamId() : null;
        String teamName = filter != null ? filter.teamName() : null;
        String countryCode = filter != null ? filter.country() : null;
        Boolean isActive = filter != null ? filter.isActive() : null;

        String sortBy = (filter != null && filter.sortBy() != null && !filter.sortBy().isBlank())
            ? filter.sortBy()
            : "name";

        String sortDir = (filter != null && filter.sortDir() != null && !filter.sortDir().isBlank())
            ? filter.sortDir()
            : "asc";

        Specification<Player> spec = Specification.allOf(
                PlayerSpecifications.nameContains(name),
                PlayerSpecifications.hasTeamId(teamId),
                PlayerSpecifications.hasTeamName(teamName),
                PlayerSpecifications.hasCountry(countryCode),
                PlayerSpecifications.isActive(isActive)
        );
        Sort sort = buildPlayerSort(sortBy, sortDir);
        return playerRepository.findAll(spec, sort);
    }

    /**
     * Builds a {@link Sort} instance for player listing.
     *
     * @param sortBy  requested sort field
     * @param sortDir requested sort direction (ASC/DESC)
     * @return a {@link Sort} configured for the requested field and direction
     */
    private Sort buildPlayerSort(String sortBy, String sortDir) {
        String key = sortBy == null ? "" : sortBy.trim();

        String property = switch (key) {
            case "goals"       -> "goals";
            case "dateOfBirth" -> "dateOfBirth";
            case "country"     -> "country.code";
            case "nameContains"    -> "team.name";
            default            -> "name";
        };

        Sort.Direction direction;
        if (sortDir == null) direction = Sort.Direction.ASC;
        else if (sortDir.equalsIgnoreCase("desc")) direction = Sort.Direction.DESC;
        else direction = Sort.Direction.ASC;
        return Sort.by(direction, property);
    }

    /**
     * Retrieves a single player by its unique identifier.
     *
     * @param id the ID of the player
     * @return the {@link Player} with the specified id
     * @throws ResponseStatusException with status 404 if the player is not found
     */
    @Override
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player "+id+" not found"));
    }

    /**
     * Creates a new player from the given request.
     * <p>
     * Validates required fields (name, dateOfBirth, country, position) and that
     * goals (if provided) are non-negative. Also checks for duplicate player name
     * and that the team exists if a teamId is provided.
     *
     * @param body the {@link PlayerDto.CreatePlayerRequest} containing player data
     * @return the newly created {@link Player} entity
     *
     * @throws ResponseStatusException with status 400 if validation fails
     * @throws ResponseStatusException with status 404 if a referenced team is not found
     * @throws ResponseStatusException with status 409 if a player with the same name already exists
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
     * <p>
     * Applies only non-null fields from {@code body}. Supported fields:
     * {@code name}, {@code dateOfBirth}, {@code country}, {@code position},
     * {@code goals}, {@code isActive}, {@code teamId}. If {@code teamId} is present,
     * it must reference an existing team.
     *
     * @param id   the id of the player to update
     * @param body partial update payload for the player
     * @return the updated {@link Player}
     *
     * @throws EntityNotFoundException
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
     * Deletes a player by its id.
     *
     * @param id the id of the player to delete
     * @throws ResponseStatusException with status 404 if the player does not exist
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
