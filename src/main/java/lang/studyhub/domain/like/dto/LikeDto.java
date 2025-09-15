package lang.studyhub.domain.like.dto;

import jakarta.validation.constraints.NotNull;

public class LikeDto {
    public record ToggleRequest(@NotNull Long userId, @NotNull Long postId) {}
    public record Response(Long postId, long likeCount, boolean likedByUser) {}
}
