package lang.studyhub.domain.bookmark.entity;

import jakarta.persistence.*;
import lang.studyhub.domain.post.entity.Post;
import lang.studyhub.domain.user.entity.User;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "bookmark")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bookmark {

    @EmbeddedId
    private BookmarkId id;

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
    void onCreate() {
        createdAt = Instant.now();
        if (id == null) {
            id = new BookmarkId(user.getId(), post.getId());
        }
    }
}
