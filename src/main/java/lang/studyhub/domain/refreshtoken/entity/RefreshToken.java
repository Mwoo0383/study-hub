package lang.studyhub.domain.refreshtoken.entity;

import jakarta.persistence.*;
import lang.studyhub.domain.user.entity.User;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "refreshtoken")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(nullable=false, unique=true, length=512)
    private String token;

    @Column(name="expires_at", nullable=false)
    private Instant expiresAt;

    @Column(name="created_at", nullable=false, updatable=false)
    private Instant createdAt;

    @PrePersist
    void onCreate() { createdAt = Instant.now(); }
}