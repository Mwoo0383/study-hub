package lang.studyhub.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PostDto {

    public record CreateRequest(
            @NotNull Long boardId,
            @NotBlank @Size(max = 200) String title,
            @NotBlank @Size(max = 200)
            @Pattern(regexp = "^[a-z0-9-]+$", message = "slug는 소문자/숫자/하이픈만 허용")
            String slug,
            @NotBlank String content,
            @Pattern(regexp = "^(DRAFT|PUBLISHED|ARCHIVED)$") String status
    ) {}

    public record UpdateRequest(
            @Size(max = 200) String title,
            @Size(max = 200) @Pattern(regexp = "^[a-z0-9-]+$") String slug,
            String content,
            @Pattern(regexp = "^(DRAFT|PUBLISHED|ARCHIVED)$") String status
    ) {}

    public record Response(
            Long id, Long boardId, String title, String slug, String content,
            String status, Long viewCount, String createdAt, String updatedAt
    ) {}
}
