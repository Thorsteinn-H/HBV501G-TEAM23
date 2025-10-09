package is.hi.hbv501gteam23.Persistence.Entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Nafn : Þorsteinn H. Erlendsson
 * Tölvupóstur: the85@hi.is
 * Lýsing:
 **/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "score")
public class Score {
    @Id
    @SequenceGenerator(name = "score_sequence", sequenceName = "score_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "score_sequence")
    @Column(name = "score_id")
    private Long id;

    @Builder.Default
    @Column(name = "homeGoals", nullable = false)
    private Integer homeGoals = 0;

    @Builder.Default
    @Column(name = "awayGoals", nullable = false)
    private Integer awayGoals = 0;
}
