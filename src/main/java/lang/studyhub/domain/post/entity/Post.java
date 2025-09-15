package lang.studyhub.domain.post.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import lang.studyhub.domain.board.entity.Board;

@Entity
@Table(name = "post",
        uniqueConstraints = @UniqueConstraint(name = "ux_post_board_slug", columnNames = {"board_id","slug"}),
        indexes = @Index(name = "idx_post_board_created_at", columnList = "board_id,created_at DESC"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 200)
    private String slug;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false, length = 20)
    private String status; // DRAFT / PUBLISHED / ARCHIVED

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        var now = Instant.now();
        createdAt = now; updatedAt = now;
        if (status == null) status = "PUBLISHED";
        if (viewCount == null) viewCount = 0L;
    }

    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }
}
