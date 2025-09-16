package lang.studyhub.domain.like.id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeId implements Serializable {
    private Long userId;
    private Long postId;

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostLikeId that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(postId, that.postId);
    }
    @Override public int hashCode() { return Objects.hash(userId, postId); }
}
