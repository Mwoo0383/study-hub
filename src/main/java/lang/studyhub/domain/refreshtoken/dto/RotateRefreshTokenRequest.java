package lang.studyhub.domain.refreshtoken.dto;

public record RotateRefreshTokenRequest(
        String oldToken,
        Long ttlSeconds // null이면 기본값 사용(14일)
) {}