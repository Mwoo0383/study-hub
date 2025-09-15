package lang.studyhub.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=50, unique=true)
    private String username;

    @Column(nullable=false, length=255)
    private String password; // BCrypt

    @Column(nullable=false, length=50, unique=true)
    private String nickname;

    @Column(nullable=false, length=100, unique=true)
    private String email;

    @Column(nullable=false, length=20)
    private String role;

    @Column(nullable=false, updatable=false)
    private Instant createdAt;
    @Column(nullable=false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        var now = Instant.now();
        createdAt = now; updatedAt = now;
        if (role == null) role = "USER";
    }
    @PreUpdate void onUpdate() { updatedAt = Instant.now(); }
}
