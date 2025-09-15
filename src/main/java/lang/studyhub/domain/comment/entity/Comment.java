package lang.studyhub.domain.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import lang.studyhub.domain.post.entity.Post;
import lang.studyhub.domain.user.entity.User;

import java.time.Instant;

@Entity
@Table(name = "comment", indexes = {
        @Index(name = "idx_comment_post_created_at", columnList = "post_id,created_at DESC")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="post_id", nullable=false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(nullable=false, columnDefinition="TEXT")
    private String content;

    @Column(nullable=false, updatable=false)
    private Instant createdAt;
    @Column(nullable=false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        var now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }
    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }
}
