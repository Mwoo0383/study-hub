package lang.studyhub.domain.attachment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "attachment")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Attachment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=255) private String originalName;
    @Column(nullable=false, length=255) private String storedName;
    @Column(nullable=false, length=100) private String contentType;
    @Column(name = "size", nullable=false) private Long fileSize;
    @Column(nullable=false, columnDefinition="TEXT") private String storageUri; // file://... or s3://...
    @Column(nullable=false) private Long uploaderId;

    @Column(nullable=false, updatable=false) private Instant createdAt;

    @PrePersist void onCreate() { createdAt = Instant.now(); }
}
