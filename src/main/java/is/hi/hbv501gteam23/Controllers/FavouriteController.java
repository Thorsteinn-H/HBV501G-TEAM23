package is.hi.hbv501gteam23.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import is.hi.hbv501gteam23.Persistence.Entities.Favorite;
import is.hi.hbv501gteam23.Persistence.Repositories.AuthRepository;
import is.hi.hbv501gteam23.Persistence.dto.FavoriteDto;
import is.hi.hbv501gteam23.Persistence.enums.FavoriteType;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
     * Resolves the ID of the currently authenticated user from the security context.
     *
     * @return the ID of the logged-in user
     * @throws IllegalStateException if there is no authenticated user
     *                               or if the user cannot be found in the database
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
     * Parses a string representation of a favorite entity type to a {@link FavoriteType}.
     *
     * @param type the type as a string (e.g. {@code "PLAYER"}, {@code "TEAM"}, {@code "MATCH"},
     *             case-insensitive)
     * @return the corresponding {@link FavoriteType}
     * @throws ResponseStatusException with status 400 (BAD_REQUEST) if the type is invalid
     */
    private FavoriteType parseType(String type) {
        try {
            return FavoriteType.valueOf(type.trim().toUpperCase());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid type. Use one of: PLAYER, TEAM, MATCH.");
        }
    }

    /**
     * Lists all favorites for the currently authenticated user.
     *
     * @return a list of favorites
     */
    @GetMapping
    @Operation(summary = "List favorites")
    public ResponseEntity<List<FavoriteDto.FavoriteResponse>> getAllFavorites() {
        Long userId = getCurrentUserId();
        var list = favoriteService.listAllForUser(userId);
        return ResponseEntity.ok(list);
    }

    /**
     * Lists favorites of a specific type for the currently authenticated user.
     *
     * @param type the type of favorite to filter by (PLAYER, TEAM, or MATCH, case-insensitive)
     * @return {@link ResponseEntity} with status 200 (OK) containing a list of
     * {@link FavoriteDto.FavoriteResponse} of the requested type for the current user
     */
    @GetMapping(value = "/{type}")
    @Operation(summary = "Get favorites by type")
    public ResponseEntity<List<FavoriteDto.FavoriteResponse>> getFavoritesByType(@ParameterObject FavoriteType type) {
        Long userId = getCurrentUserId();
        var list = favoriteService.listForUserAndType(userId, type);
        return ResponseEntity.ok(list);
    }

    /**
     * Checks whether a given item is a favorite for the currently authenticated user.
     *
     * @param request  request body containing the id and type of the item to favorite
     * @return {@link ResponseEntity} with status 200 (OK) containing {@code true}
     * if the item is a favorite, otherwise {@code false}
     */
    @PostMapping("/{type}/{itemId}")
    @Operation(summary = "Add to favorites")
    public ResponseEntity<FavoriteDto.FavoriteResponse> addFavorite(
            @RequestBody FavoriteDto.CreateFavoriteRequest request
    ) {
        Long userId = getCurrentUserId();
        FavoriteDto.FavoriteResponse created = favoriteService.addFavorite(
                userId,
                request.favoriteType(),
                request.favoriteId());

        var location = URI.create(String.format("/favorites/%s/%d", request.favoriteType(), request.favoriteId()));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, location.toString())
                .body(created);
    }

    /**
     * Adds the given item to the favorites of the currently authenticated user.
     *
     * @param type   the type of the item (PLAYER, TEAM, or MATCH, case-insensitive)
     * @param itemId the ID of the item to add as a favorite
     * @return {@link ResponseEntity} with status 201 (CREATED) containing the created
     * favorite representation in the body and a {@code Location} header pointing
     * to /favorites/{type}/{itemId}
     */
    @DeleteMapping("/{type}/{itemId}")
    @Operation(summary = "Remove favorite")
    public ResponseEntity<Void> removeFavorite(@PathVariable String type, @PathVariable Long itemId) {
        Long userId = getCurrentUserId();
        FavoriteType favoriteType = parseType(type);
        favoriteService.removeFavorite(userId, favoriteType, itemId);
        return ResponseEntity.noContent().build();
    }
}
