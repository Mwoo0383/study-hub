package lang.studyhub.domain.bookmark.dto;

import java.time.Instant;

public record BookmarkResponse(
        Long postId,
        String postTitle,
        Long boardId,
        Long userId,
        Instant createdAt
) {}
