package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Favorites;
import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.Repositories.AuthRepository;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes read/write operations for {@link Favorites} resources.
 * Base path is /favoerites
 */
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavouriteController {
    private final FavoriteService favoriteService;
    private final AuthRepository authRepository;

    /**
     * Get the current logged in users id
     * @return the id of the logged in user
     */
    private Long getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new IllegalStateException("Not authenticated");
        }
        String email = auth.getName();
        return authRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found: " + email))
                .getId();
    }

    /**
     * Add an item to user's favorites
     * @param type The type of the favorite (match, player, score, team, venue)
     * @param itemId The ID of the item to add
     * @return
     */

    @PostMapping("/{type}/{itemId}")
    public ResponseEntity<?> addFavorite(
            @PathVariable String type,
            @PathVariable Long itemId) {
        try {
            Long userId = getCurrentUserId();
            favoriteService.addFavorite(userId, type, itemId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding to favorites: " + e.getMessage());
        }
    }

    /**
     *
     * @param type
     * @param itemId
     * @return
     */
    @DeleteMapping("/{type}/{itemId}")
    public ResponseEntity<?> removeFavorite(
            @PathVariable String type,
            @PathVariable Long itemId) {
        try {
            Long userId = getCurrentUserId();
            favoriteService.removeFavorite(userId, type, itemId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error removing from favorites: " + e.getMessage());
        }
    }

    /**
     *
     * @param type e
     * @return
     */
    @GetMapping("/{type}")
    public ResponseEntity<?> getFavorites(@PathVariable String type) {
        try {
            Long userId = getCurrentUserId();
            List<Long> favorites = favoriteService.getFavorites(userId, type);
            return ResponseEntity.ok(favorites);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting favorites: " + e.getMessage());
        }
    }

    /**
     *
     * @param type
     * @param itemId
     * @return
     */
    @GetMapping("/{type}/{itemId}")
    public ResponseEntity<Boolean> isFavorite(
            @PathVariable String type,
            @PathVariable Long itemId) {
        Long userId = getCurrentUserId();
        boolean isFav = favoriteService.isFavorite(userId, type, itemId);
        return ResponseEntity.ok(isFav);
    }
}
