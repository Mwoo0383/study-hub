package lang.studyhub.domain.like.entity;

import jakarta.persistence.*;
import lang.studyhub.domain.like.id.PostLikeId;
import lang.studyhub.domain.post.entity.Post;
import lang.studyhub.domain.user.entity.User;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "postlike")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PostLike {

    @EmbeddedId
    private PostLikeId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("postId")
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() { createdAt = Instant.now(); }
}
