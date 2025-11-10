package is.hi.hbv501gteam23.Persistence.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
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

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @Column(name = "is_active", nullable = false)
    private boolean isActive = false;

    @JsonIgnore
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @JsonIgnore
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "role", nullable = false)
    private String role;

    @Lob
    @Column(name="image")
    private byte[] image;

    @Column(name="imageType")
    private String imageType;
}
