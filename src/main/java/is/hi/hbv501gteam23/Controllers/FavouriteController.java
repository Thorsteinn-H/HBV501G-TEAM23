package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.Favorite;
import is.hi.hbv501gteam23.Persistence.Repositories.AuthRepository;
import is.hi.hbv501gteam23.Persistence.dto.FavoriteDto;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

/**
 * REST controller that exposes read/write operations for {@link Favorite} resources.
 * Base path is /favorites
 */
@Tag(name = "Favorite")
@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavouriteController {
    private final FavoriteService favoriteService;
    private final AuthRepository authRepository;

    /**
     * Get the currently logged-in users id
     * @return the id of the logged-in user
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

    private Favorite.EntityType parseType(String type) {
        try {
            return Favorite.EntityType.valueOf(type.trim().toUpperCase());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid type. Use one of: PLAYER, TEAM, MATCH.");
        }
    }

    @GetMapping
    @Operation(summary = "List favorites")
    public ResponseEntity<List<FavoriteDto.FavoriteResponse>> getAllFavorites() {
        Long userId = getCurrentUserId();
        var list = favoriteService.listAllForUser(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{type}")
    @Operation(summary = "Get favorites by type")
    public ResponseEntity<List<FavoriteDto.FavoriteResponse>> getFavoritesByType(@PathVariable String type) {
        Long userId = getCurrentUserId();
        var entityType = parseType(type);
        var list = favoriteService.listForUserAndType(userId, entityType);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{type}/{itemId}")
    public ResponseEntity<Boolean> isFavorite(
            @PathVariable String type,
            @PathVariable Long itemId) {
        Long userId = getCurrentUserId();
        var entityType = parseType(type);
        boolean isFav = favoriteService.isFavorite(userId, entityType, itemId);
        return ResponseEntity.ok(isFav);
    }


    @PostMapping("/{type}/{itemId}")
    @Operation(summary = "Add to favorites")
    public ResponseEntity<?> addFavorite(
            @PathVariable String type,
            @PathVariable Long itemId) {
        Long userId = getCurrentUserId();
        var entityType = parseType(type);

        var created = favoriteService.addFavorite(userId, entityType, itemId);
        var location = URI.create(String.format("/favorites/%s/%d", entityType, itemId));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, location.toString())
                .body(created);
    }

    @DeleteMapping("/{type}/{itemId}")
    @Operation(summary = "Remove favorite")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable String type,
            @PathVariable Long itemId) {
        Long userId = getCurrentUserId();
        var entityType = parseType(type);
        favoriteService.removeFavorite(userId, entityType, itemId);
        return ResponseEntity.noContent().build();
    }
}
