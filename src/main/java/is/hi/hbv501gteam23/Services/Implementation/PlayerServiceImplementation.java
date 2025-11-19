package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.Entities.Team;
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

    /**
     * Finds players using optional filters, with sorting and pagination.
     * All filter parameters are optional; when {@code null} or blank, they are ignored.
     *
     * @param name     player name to filter by
     * @param teamId   team ID to filter by
     * @param teamName team name filter
     * @param country  country code to filter by
     * @param isActive active status filter
     * @param sortBy   field to sort by
     * @param sortDir  sort direction, either {@code "ASC"} or {@code "DESC"}
     * @param page     zero-based page index
     * @param size     page size (max number of players per page)
     * @return list of {@link Player} entities matching the given filters
     */
    @Override
    public List<Player> findPlayers(
            String name,
            Long teamId,
            String teamName,
            String country,
            Boolean isActive,
            String sortBy,
            String sortDir,
            int page,
            int size
    ) {
        Specification<Player> spec = (root, query, criteriaBuilder) -> {
            Predicate p = criteriaBuilder.conjunction();

            if (name != null && !name.isBlank()) {
                p = criteriaBuilder.and(p, criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (teamId != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("team").get("id"), teamId));
            }

            if (teamName != null && !teamName.isBlank()) {
                p = criteriaBuilder.and(p, criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("team").get("name")), "%" + teamName.toLowerCase() + "%"));
            }

            if (country != null && !country.isBlank()) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(
                        root.get("country"), country.toUpperCase()));
            }

            if (isActive != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("active"), isActive));
            }

            return p;
        };

        Sort.Direction direction = Sort.Direction.ASC;
        if ("DESC".equalsIgnoreCase(sortDir)) {
            direction = Sort.Direction.DESC;
        }

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
     * Retrieves a single player by name.
     *
     * @param name the name (or part of a name) of the player
     * @return the {@link Player} matching the name
     * @throws ResponseStatusException with status 404 if no player is found
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
     * Retrieves a list of players by their team name.
     *
     * @param teamName the name of the team
     * @return a list of {@link Player} entities that belong to the specified team name
     * @throws ResponseStatusException with status 404 if no players are found for the team name
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
     * Retrieves a list of players by their team's unique identifier.
     *
     * @param teamId the ID of the team
     * @return a list of {@link Player} entities belonging to the specified team
     * @throws ResponseStatusException with status 404 if the team does not exist
     */
    @Override
    public List<Player> getByTeamId(Long teamId) {
        if (teamRepository.existsById(teamId)) {
            return playerRepository.findByTeamId(teamId);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team "+teamId+" not found");
    }

    /**
     * Retrieves players by their active status.
     *
     * @param isActive the active status to filter by
     * @return a list of {@link Player} entities with the given active status
     */
    @Override
    public List<Player> getActivePlayers(Boolean isActive) {
        return playerRepository.findByIsActive(isActive);
    }

    /**
     * Retrieves players by country.
     *
     * @param country the country to filter by
     * @return a list of {@link Player} entities from the given country
     */
    @Override
    public List<Player> findPlayerByCountry(String country) {
        return playerRepository.findByCountry(country);
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
        if (body == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required");
        }

        String name = body.name() == null ? null : body.name().trim();
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player name is required");
        }
        if (body.dateOfBirth() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dateOfBirth is required");
        }
        if (body.dateOfBirth().isAfter(java.time.LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dateOfBirth cannot be in the future");
        }
        if (body.goals() != null && body.goals() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "goals must be >= 0");
        }
        String country = body.country() == null ? null : body.country().trim();
        if (country == null || country.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "country is required");
        }
        if (body.position() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "position is required");
        }

        Player existing = playerRepository.findByNameContainingIgnoreCase(name);
        if (existing != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Player " + name + " already exists");
        }

        Team team = null;
        if (body.teamId() != null) {
            team = teamRepository.findById(body.teamId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team " + body.teamId() + " not found"));
        }

        Player p = new Player();
        p.setName(name);
        p.setDateOfBirth(body.dateOfBirth());
        p.setCountry(MetadataUtils.normalizeCountryCode(body.country()));
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
        if (body.country() != null)     p.setCountry(MetadataUtils.normalizeCountryCode(body.country()));
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
