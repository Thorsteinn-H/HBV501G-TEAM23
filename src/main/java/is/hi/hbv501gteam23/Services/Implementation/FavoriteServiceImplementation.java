package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Favorite;
import is.hi.hbv501gteam23.Persistence.Repositories.FavoriteRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.MatchRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.PlayerRepository;
import is.hi.hbv501gteam23.Persistence.Repositories.TeamRepository;
import is.hi.hbv501gteam23.Persistence.dto.FavoriteDto;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * Service implementation for handling favorite items.
 * Provides operations for adding, removing, checking and listing favorites
 * for a given user across different entity types (players, teams, matches).
 */
@Service
@RequiredArgsConstructor
public class FavoriteServiceImplementation implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    /**
     * Adds a new favorite item for the given user and entity.
     *
     * @param userId   the ID of the user adding the favorite
     * @param type     the type of the favorited entity (MATCH, PLAYER, TEAM)
     * @param entityId the ID of the entity to mark as favorite
     * @return a {@link FavoriteDto.favoriteResponse} representing the created favorite
     * @throws ResponseStatusException with status 404 if the target entity does not exist
     * @throws ResponseStatusException with status 409 if the favorite already exists
     */
    @Override
    @Transactional
    public FavoriteDto.FavoriteResponse addFavorite(Long userId, Favorite.EntityType type, Long entityId) {
        boolean targetExists = switch (type) {
            case MATCH  -> matchRepository.existsById(entityId);
            case PLAYER -> playerRepository.existsById(entityId);
            case TEAM   -> teamRepository.existsById(entityId);
        };
        if (!targetExists) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, type + " " + entityId + " not found");
        }

        var existing = favoriteRepository
                .findByUserIdAndEntityTypeAndEntityId(userId, type, entityId);
        if (existing.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Already in favorites");
        }

        Favorite f = new Favorite();
        f.setUserId(userId);
        f.setEntityType(type);
        f.setEntityId(entityId);

        Favorite saved = favoriteRepository.save(f);
        return toResponse(saved);
    }

    /**
     * Removes an existing favorite for the given user and entity.
     *
     * @param userId   the ID of the user
     * @param type     the type of the favorited entity
     * @param entityId the ID of the entity whose favorite should be removed
     * @throws ResponseStatusException with status 404 if the favorite does not exist
     */
    @Override
    public void removeFavorite(Long userId, Favorite.EntityType type, Long entityId) {
        var existing = favoriteRepository.findByUserIdAndEntityTypeAndEntityId(userId, type, entityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Favorite not found"));
        favoriteRepository.delete(existing);
    }

    /**
     * Checks whether a given entity is a favorite for the user.
     *
     * @param userId   the ID of the user
     * @param type     the type of the entity
     * @param entityId the ID of the entity
     * @return {@code true} if the entity is a favorite for the user, otherwise {@code false}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isFavorite(Long userId, Favorite.EntityType type, Long entityId) {
        return favoriteRepository.existsByUserIdAndEntityTypeAndEntityId(userId, type, entityId);
    }

    /**
     * Lists all favorites for a given user.
     *
     * @param userId the ID of the user
     * @return a list of {@link FavoriteDto.favoriteResponse} representing all favorites of the user
     */
    @Override
    @Transactional(readOnly = true)
    public List<FavoriteDto.FavoriteResponse> listAllForUser(Long userId) {
        return favoriteRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Lists all favorites for a given user filtered by entity type.
     *
     * @param userId the ID of the user
     * @param type   the type of the favorited entities to list
     * @return a list of {@link FavoriteDto.favoriteResponse} for the given user and type
     */
    @Override
    @Transactional(readOnly = true)
    public List<FavoriteDto.FavoriteResponse> listForUserAndType(Long userId, Favorite.EntityType type) {
        return favoriteRepository.findByUserIdAndEntityType(userId, type).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Maps a {@link Favorite} entity to a {@link FavoriteDto.favoriteResponse} DTO.
     *
     * @param f the favorite entity to map
     * @return the mapped {@link FavoriteDto.favoriteResponse}
     */
    private FavoriteDto.FavoriteResponse toResponse(Favorite f) {
        return new FavoriteDto.FavoriteResponse(
                f.getId(),
                f.getUserId(),
                f.getEntityType(),
                f.getEntityId()
        );
    }
}
