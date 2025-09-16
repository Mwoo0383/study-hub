package lang.studyhub.domain.refreshtoken.dto;

import java.time.Instant;

public record TokenResponse(
        String token,
        Instant expiresAt
) {}
