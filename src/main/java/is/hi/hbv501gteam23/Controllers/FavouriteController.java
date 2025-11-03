package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Favorites;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavouriteController {
    private final FavoriteService favoriteService;

    public FavouriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Assuming the user ID is stored in the authentication principal
        // You might need to adjust this based on your authentication setup
        return Long.parseLong(authentication.getName());
    }

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

    @GetMapping("/{type}/{itemId}")
    public ResponseEntity<Boolean> isFavorite(
            @PathVariable String type,
            @PathVariable Long itemId) {
        Long userId = getCurrentUserId();
        boolean isFav = favoriteService.isFavorite(userId, type, itemId);
        return ResponseEntity.ok(isFav);
    }
}
