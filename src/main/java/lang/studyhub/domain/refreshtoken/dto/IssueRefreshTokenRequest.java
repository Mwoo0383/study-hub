package lang.studyhub.domain.refreshtoken.dto;

public record IssueRefreshTokenRequest(
        Long ttlSeconds // null이면 기본값 사용(14일)
) {}
