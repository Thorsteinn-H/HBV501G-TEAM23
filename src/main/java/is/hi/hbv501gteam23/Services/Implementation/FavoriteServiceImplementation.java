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
 * Service implementation for handling favorite items
 */
@Service
@RequiredArgsConstructor
public class FavoriteServiceImplementation implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public FavoriteDto.favoriteResponse addFavorite(Long userId, Favorite.EntityType type, Long entityId) {
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


    @Override
    public void removeFavorite(Long userId, Favorite.EntityType type, Long entityId) {
        var existing = favoriteRepository.findByUserIdAndEntityTypeAndEntityId(userId, type, entityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Favorite not found"));
        favoriteRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFavorite(Long userId, Favorite.EntityType type, Long entityId) {
        return favoriteRepository.existsByUserIdAndEntityTypeAndEntityId(userId, type, entityId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteDto.favoriteResponse> listAllForUser(Long userId) {
        return favoriteRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteDto.favoriteResponse> listForUserAndType(Long userId, Favorite.EntityType type) {
        return favoriteRepository.findByUserIdAndEntityType(userId, type).stream()
                .map(this::toResponse)
                .toList();
    }

    private FavoriteDto.favoriteResponse toResponse(Favorite f) {
        return new FavoriteDto.favoriteResponse(
                f.getId(),
                f.getUserId(),
                f.getEntityType(),
                f.getEntityId()
        );
    }
}