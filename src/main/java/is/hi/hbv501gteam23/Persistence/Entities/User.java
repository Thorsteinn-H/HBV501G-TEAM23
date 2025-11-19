package is.hi.hbv501gteam23.Persistence.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import is.hi.hbv501gteam23.Persistence.enums.Gender;
import is.hi.hbv501gteam23.Persistence.enums.SystemRole;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@FilterDef(name = "activeUserFilter", parameters = @ParamDef(name = "isActive", type = Boolean.class))
@Filter(name = "activeUserFilter", condition = "is_active = :isActive")
public class User implements Serializable {
    @Serial
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

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = true, columnDefinition = "gender_enum")
    private Gender gender;

    @JsonIgnore
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @JsonIgnore
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "role_enum")
    private SystemRole role;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "profile_image_id")
    private Image profileImage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
