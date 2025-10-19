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
@Table(name = "users")
public class User {
    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", nullable = false, length = 120)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 320)
    private String email;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "createdAt", nullable = false)
    private LocalDate createdAt;

    @Column(name = "passwordHash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "role", nullable = false)
    private String role;
}
