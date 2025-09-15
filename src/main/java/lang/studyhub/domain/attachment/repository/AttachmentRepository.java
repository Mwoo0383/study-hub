package lang.studyhub.domain.attachment.repository;

import lang.studyhub.domain.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> { }
