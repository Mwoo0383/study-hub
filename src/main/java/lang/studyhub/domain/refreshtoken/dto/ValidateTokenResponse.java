package lang.studyhub.domain.refreshtoken.dto;

import java.time.Instant;

public record ValidateTokenResponse(
        boolean valid,
        Instant expiresAt
) {}
