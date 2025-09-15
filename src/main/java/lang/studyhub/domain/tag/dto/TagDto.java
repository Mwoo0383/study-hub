package lang.studyhub.domain.tag.dto;

import jakarta.validation.constraints.NotBlank;

public class TagDto {

    public record CreateRequest(@NotBlank String name) {}

    public record Response(Long id, String name) {}
}
