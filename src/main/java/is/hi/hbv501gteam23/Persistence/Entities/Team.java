package is.hi.hbv501gteam23.Persistence.Entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team")
public class Team {
    @Id
    @SequenceGenerator(name = "team_sequence", sequenceName = "team_sequence",  allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_sequence")
    @Column(name = "team_id")
    private Long id;

    @Column(name = "team_name", nullable = false, length = 120)
    private String name;

    @Column(name = "team_country", nullable = false, length = 80)
    private String country;

    @Column(name = "venue_id")
    private Long venueId;
}
