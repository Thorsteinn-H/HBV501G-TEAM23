package is.hi.hbv501gteam23.Persistence.Entities;

import is.hi.hbv501gteam23.Persistence.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "player")
public class Player {
    @Id
    @SequenceGenerator(name = "player_sequence", sequenceName = "player_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "player_sequence")
    @Column(name = "player_id")
    private Long id;

    @Column(name = "player_name", nullable = false, length = 120)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "date_of_birth",  nullable = true)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = true)
    private Gender gender;

    @Column(name = "player_country", length = 80)
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public enum PlayerPosition { GOALKEEPER, DEFENDER, MIDFIELDER, FORWARD }
    @Enumerated(EnumType.STRING)
    @Column(name = "player_position", length = 40, nullable = false)
    private PlayerPosition position;

    @Column(name = "goals", nullable = false)
    private Integer goals;
}
