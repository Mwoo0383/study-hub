package lang.studyhub.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BoardDto {

    public record CreateRequest(
            @NotBlank @Size(max = 100) String name,
            @NotBlank @Size(max = 100)
            @Pattern(regexp = "^[a-z0-9-]+$", message = "slug는 소문자/숫자/하이픈만 허용")
            String slug,
            @Size(max = 2000) String description,
            @Pattern(regexp = "^(PUBLIC|PRIVATE)$", message = "visibility는 PUBLIC/PRIVATE")
            String visibility,
            Integer sortOrder
    ) {}

    public record UpdateRequest(
            @Size(max = 100) String name,
            @Size(max = 100)
            @Pattern(regexp = "^[a-z0-9-]+$") String slug,
            @Size(max = 2000) String description,
            @Pattern(regexp = "^(PUBLIC|PRIVATE)$") String visibility,
            Integer sortOrder
    ) {}

    public record Response(
            Long id, String name, String slug, String description,
            String visibility, Integer sortOrder
    ) {}
}
