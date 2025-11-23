package is.hi.hbv501gteam23.Persistence.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @SequenceGenerator(name = "match_sequence", sequenceName = "match_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_sequence")
    @Column(name = "match_id")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd['T'HH:mm:ssXXX]")
    @Column(name = "match_date", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime matchDate;

    @ManyToOne(fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.EAGER,   optional = false)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @ManyToOne(fetch = FetchType.EAGER,   optional = false)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Builder.Default
    @Column(name = "home_goals", nullable = false)
    private Integer homeGoals = 0;

    @Builder.Default
    @Column(name = "away_goals", nullable = false)
    private Integer awayGoals = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
