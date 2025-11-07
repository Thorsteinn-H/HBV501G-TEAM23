package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.Favorites;

import java.util.List;

/**
 * Service interface for handling user favorites
 */
public interface FavoriteService {
    /**
     * Get or create favorites for a user
     * @param userId The ID of the user
     * @return The user's favorites
     */
    Favorites getOrCreateFavorites(Long userId);
    
    /**
     * Add an item to user's favorites
     * @param userId The ID of the user
     * @param type The type of the favorite (match, player, score, team, venue)
     * @param itemId The ID of the item to add
     */
    void addFavorite(Long userId, String type, Long itemId);
    
    /**
     * Remove an item from user's favorites
     * @param userId The ID of the user
     * @param type The type of the favorite (match, player, score, team, venue)
     * @param itemId The ID of the item to remove
     */
    void removeFavorite(Long userId, String type, Long itemId);
    
    /**
     * Get all favorites of a specific type for a user
     * @param userId The ID of the user
     * @param type The type of favorites to get (match, player, score, team, venue)
     * @return List of favorite item IDs
     */
    List<Long> getFavorites(Long userId, String type);
    
    /**
     * Check if an item is in user's favorites
     * @param userId The ID of the user
     * @param type The type of the favorite (match, player, score, team, venue)
     * @param itemId The ID of the item to check
     * @return true if the item is in favorites, false otherwise
     */
    boolean isFavorite(Long userId, String type, Long itemId);
}
