package lang.studyhub.domain.tag.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import lang.studyhub.domain.post.entity.Post;

@Entity
@Table(name = "tag")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=50)
    private String name;

    @Column(nullable=false, updatable=false)
    private Instant createdAt;

    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();

    @PrePersist
    void onCreate() { createdAt = Instant.now(); }
}
