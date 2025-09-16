package lang.studyhub.domain.refreshtoken.repository;


import lang.studyhub.domain.refreshtoken.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    boolean existsByToken(String token);
    long deleteByToken(String token);
    long deleteByUserId(Long userId);
}