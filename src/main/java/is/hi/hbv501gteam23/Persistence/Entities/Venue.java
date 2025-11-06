package is.hi.hbv501gteam23.Persistence.Entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "venues")
public class Venue {
    @Id
    @SequenceGenerator(name = "venue_sequence", sequenceName = "venue_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "venue_sequence")
    @Column(name = "venue_id")
    private Long id;

    @Column(name = "venue_name", nullable = false, length = 160)
    private String name;

    @Column(name = "address")
    private String address;
}
