package is.hi.hbv501gteam23.Persistence.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "match")
public class Match {
    @Id
    @SequenceGenerator(name = "match_sequence", sequenceName = "match_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_sequence")
    @Column(name = "match_id")
    private Long id;

    @Column(name = "match_date", nullable = false)
    private LocalDate date;

    @Column(name = "home_goals", nullable = false)
    private Integer homeGoals = 0;

    @Column(name = "away_goals", nullable = false)
    private Integer awayGoals = 0;

    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY,   optional = false)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @ManyToOne(fetch = FetchType.LAZY,   optional = false)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;
}
