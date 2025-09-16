package lang.studyhub.domain.bookmark.dto;

import jakarta.validation.constraints.NotNull;

public record BookmarkCreateRequest(
        @NotNull Long postId
) {}
