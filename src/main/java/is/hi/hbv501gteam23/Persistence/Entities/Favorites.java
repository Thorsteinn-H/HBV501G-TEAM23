package is.hi.hbv501gteam23.Persistence.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "favorites")
public class Favorites {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String matches; // Comma-separated match IDs

    @Column(columnDefinition = "TEXT")
    private String players; // Comma-separated player IDs
    
    @Column(columnDefinition = "TEXT")
    private String scores; // Comma-separated score IDs
    
    @Column(columnDefinition = "TEXT")
    private String teams; // Comma-separated team IDs
    
    @Column(columnDefinition = "TEXT")
    private String venues; // Comma-separated venue IDs
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
