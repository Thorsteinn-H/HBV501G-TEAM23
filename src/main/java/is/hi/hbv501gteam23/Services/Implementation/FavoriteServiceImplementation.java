package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Favorites;
import is.hi.hbv501gteam23.Persistence.Repositories.FavoriteRepository;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service implementation for handling favorite items
 */
@Service
public class FavoriteServiceImplementation implements FavoriteService {
    private final FavoriteRepository favoriteRepository;

    @Autowired
    public FavoriteServiceImplementation(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public Favorites getOrCreateFavorites(Long userId) {
        return favoriteRepository.findById(userId)
                .orElseGet(() -> {
                    Favorites newFavorites = new Favorites();
                    newFavorites.setUserId(userId);
                    return favoriteRepository.save(newFavorites);
                });
    }

    @Override
    @Transactional
    public void addFavorite(Long userId, String type, Long itemId) {
        Favorites favorites = getOrCreateFavorites(userId);
        String currentIds = getFieldByType(favorites, type);
        
        if (currentIds == null || currentIds.isEmpty()) {
            setFieldByType(favorites, type, String.valueOf(itemId));
        } else if (!Arrays.asList(currentIds.split(",")).contains(String.valueOf(itemId))) {
            setFieldByType(favorites, type, currentIds + "," + itemId);
        }
        
        favoriteRepository.save(favorites);
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, String type, Long itemId) {
        favoriteRepository.findById(userId).ifPresent(favorites -> {
            String currentIds = getFieldByType(favorites, type);
            if (currentIds != null && !currentIds.isEmpty()) {
                List<String> idList = new ArrayList<>(Arrays.asList(currentIds.split(",")));
                if (idList.remove(String.valueOf(itemId))) {
                    setFieldByType(favorites, type, String.join(",", idList));
                    favoriteRepository.save(favorites);
                }
            }
        });
    }

    @Override
    public List<Long> getFavorites(Long userId, String type) {
        return favoriteRepository.findById(userId)
                .map(favorites -> {
                    String ids = getFieldByType(favorites, type);
                    if (ids == null || ids.isEmpty()) {
                        return Collections.<Long>emptyList();
                    }
                    return Arrays.stream(ids.split(","))
                            .map(Long::parseLong)
                            .toList();
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public boolean isFavorite(Long userId, String type, Long itemId) {
        return favoriteRepository.findById(userId)
                .map(favorites -> {
                    String ids = getFieldByType(favorites, type);
                    return ids != null && Arrays.asList(ids.split(",")).contains(String.valueOf(itemId));
                })
                .orElse(false);
    }

    private String getFieldByType(Favorites favorites, String type) {
        return switch (type.toLowerCase()) {
            case "match" -> favorites.getMatches();
            case "player" -> favorites.getPlayers();
            case "score" -> favorites.getScores();
            case "team" -> favorites.getTeams();
            case "venue" -> favorites.getVenues();
            default -> throw new IllegalArgumentException("Invalid favorite type: " + type);
        };
    }

    private void setFieldByType(Favorites favorites, String type, String value) {
        switch (type.toLowerCase()) {
            case "match" -> favorites.setMatches(value);
            case "player" -> favorites.setPlayers(value);
            case "score" -> favorites.setScores(value);
            case "team" -> favorites.setTeams(value);
            case "venue" -> favorites.setVenues(value);
            default -> throw new IllegalArgumentException("Invalid favorite type: " + type);
        }
    }
}
