package lang.studyhub.domain.attachment.dto;

public class AttachmentDto {
    public record Response(Long id, String originalName, String contentType, Long fileSize, String storageUri) {}
}
