package lang.studyhub.domain.refreshtoken.service;

import jakarta.persistence.EntityNotFoundException;
import lang.studyhub.domain.refreshtoken.dto.*;
import lang.studyhub.domain.refreshtoken.entity.RefreshToken;
import lang.studyhub.domain.refreshtoken.repository.RefreshTokenRepository;
import lang.studyhub.domain.user.entity.User;
import lang.studyhub.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private static final long DEFAULT_TTL_SECONDS = 60L * 60L * 24L * 14L; // 14 days

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public TokenResponse issue(Long userId, IssueRefreshTokenRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        long ttl = req != null && req.ttlSeconds() != null ? req.ttlSeconds() : DEFAULT_TTL_SECONDS;
        Instant expiresAt = Instant.now().plusSeconds(ttl);

        String token = UUID.randomUUID().toString();

        RefreshToken saved = refreshTokenRepository.save(
                RefreshToken.builder()
                        .user(user)
                        .token(token)
                        .expiresAt(expiresAt)
                        .build()
        );
        return new TokenResponse(saved.getToken(), saved.getExpiresAt());
    }

    @Transactional
    public TokenResponse rotate(RotateRefreshTokenRequest req) {
        if (req == null || req.oldToken() == null || req.oldToken().isBlank()) {
            throw new IllegalArgumentException("oldToken is required");
        }

        RefreshToken old = refreshTokenRepository.findByToken(req.oldToken())
                .orElseThrow(() -> new EntityNotFoundException("Refresh token not found"));

        // (선택) 만료된 토큰이면 거부
        if (old.getExpiresAt().isBefore(Instant.now())) {
            // 만료 토큰은 삭제
            refreshTokenRepository.delete(old);
            throw new IllegalStateException("Refresh token expired");
        }

        long ttl = (req.ttlSeconds() != null) ? req.ttlSeconds() : DEFAULT_TTL_SECONDS;
        Instant expiresAt = Instant.now().plusSeconds(ttl);
        String newToken = UUID.randomUUID().toString();

        // 회전: 기존 토큰 삭제 후 새 토큰 발급
        refreshTokenRepository.delete(old);

        RefreshToken saved = refreshTokenRepository.save(
                RefreshToken.builder()
                        .user(old.getUser())
                        .token(newToken)
                        .expiresAt(expiresAt)
                        .build()
        );
        return new TokenResponse(saved.getToken(), saved.getExpiresAt());
    }

    public ValidateTokenResponse validate(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(rt -> new ValidateTokenResponse(rt.getExpiresAt().isAfter(Instant.now()), rt.getExpiresAt()))
                .orElseGet(() -> new ValidateTokenResponse(false, null));
    }

    @Transactional
    public void revoke(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    @Transactional
    public void revokeAllByUser(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
