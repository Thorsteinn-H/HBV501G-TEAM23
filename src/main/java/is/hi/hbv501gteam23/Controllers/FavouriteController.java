package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Favorites;
import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
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
public class FavouriteController {
    private final FavoriteService favoriteService;

    public FavouriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    /**
     * Get the current logged in users id
     * @return the id of the logged in user
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Assuming the user ID is stored in the authentication principal
        // You might need to adjust this based on your authentication setup
        return Long.parseLong(authentication.getName());
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
     * Remove an item from user's favorites
     * @param type The type of the favorite (match, player, score, team, venue)
     * @param itemId The ID of the item to remove
     * @return response entity if deleteing the favorite was a success
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
     * Get all favorites of a specific type for a user
     * @param type The type of favorites to get (match, player, score, team, venue)
     * @return response entity containing all users favorites
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
     * Check if an item is in user's favorites
     * @param type The type of the favorite (match, player, score, team, venue)
     * @param itemId The ID of the item to check
     * @return a response entity with true if it is favorite and false if not
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
