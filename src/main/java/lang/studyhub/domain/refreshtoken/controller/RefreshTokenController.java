package lang.studyhub.domain.refreshtoken.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lang.studyhub.domain.refreshtoken.dto.*;
import lang.studyhub.domain.refreshtoken.dto.IssueRefreshTokenRequest;
import lang.studyhub.domain.refreshtoken.dto.TokenResponse;
import lang.studyhub.domain.refreshtoken.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "RefreshTokens", description="토큰 API")
@RestController
@RequestMapping("/api/auth/refresh-tokens")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    // 임시: X-USER-ID 헤더에서 사용자 추출 (보안 붙이면 Principal 사용)
    private Long currentUserId(String header) {
        if (header == null || header.isBlank()) throw new IllegalArgumentException("X-USER-ID header required");
        return Long.parseLong(header);
    }

    @PostMapping
    public ResponseEntity<TokenResponse> issue(
            @RequestHeader("X-USER-ID") String userIdHeader,
            @Valid @RequestBody(required = false) IssueRefreshTokenRequest request
    ) {
        Long userId = currentUserId(userIdHeader);
        return ResponseEntity.ok(refreshTokenService.issue(userId, request));
    }

    @PostMapping("/rotate")
    public ResponseEntity<TokenResponse> rotate(
            @Valid @RequestBody RotateRefreshTokenRequest request
    ) {
        return ResponseEntity.ok(refreshTokenService.rotate(request));
    }

    @GetMapping("/{token}")
    public ResponseEntity<ValidateTokenResponse> validate(@PathVariable String token) {
        return ResponseEntity.ok(refreshTokenService.validate(token));
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<Void> revoke(@PathVariable String token) {
        refreshTokenService.revoke(token);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> revokeAllByUser(@PathVariable Long userId) {
        refreshTokenService.revokeAllByUser(userId);
        return ResponseEntity.noContent().build();
    }
}