package lang.studyhub.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommentDto {

    public record CreateRequest(
            @NotNull Long postId,
            @NotNull Long userId,
            @NotBlank String content
    ) {}

    public record Response(
            Long id, Long postId, Long userId,
            String content, String createdAt, String updatedAt
    ) {}
}
