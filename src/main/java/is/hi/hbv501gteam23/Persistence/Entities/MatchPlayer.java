package is.hi.hbv501gteam23.Persistence.Entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "match_player")
public class MatchPlayer {
    @Id
    @SequenceGenerator(name = "matchPlayer_sequence", sequenceName = "matchPlayer_sequence", allocationSize = 1 )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "matchPlayer_sequence")
    @Column(name = "matchPlayer_id")
    private Long id;

    @Column(name = "match_id")
    private Long matchId;

    @Column(name = "player_id")
    private Long playerId;

    @Builder.Default
    @Column(name = "goalsInMatch")
    private Integer goalsInMatch = 0;
}
