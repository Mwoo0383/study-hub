package lang.studyhub.domain.bookmark.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class BookmarkId implements Serializable {
    private Long userId;
    private Long postId;
}
