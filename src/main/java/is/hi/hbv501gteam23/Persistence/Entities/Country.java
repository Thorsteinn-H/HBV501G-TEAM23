package is.hi.hbv501gteam23.Persistence.Entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "countries")
public class Country {

    @Id
    @Column(length = 2)
    private String code;

    @Column(name = "country_name", nullable = false)
    private String countryName;
}
