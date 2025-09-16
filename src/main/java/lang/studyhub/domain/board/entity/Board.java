package lang.studyhub.domain.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "board", indexes = {
        @Index(name = "idx_board_name", columnList = "name")
}, uniqueConstraints = {
        @UniqueConstraint(name = "ux_board_slug", columnNames = "slug")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String slug; // 영문/하이픈

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 20)
    private String visibility; // PUBLIC / PRIVATE

    @Column(nullable = false)
    private Integer sortOrder;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        var now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (visibility == null) visibility = "PUBLIC";
        if (sortOrder == null) sortOrder = 0;
    }

    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }
}
