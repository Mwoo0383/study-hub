package lang.studyhub.domain.user.service;

import lang.studyhub.domain.user.dto.UserDto.*;
import lang.studyhub.domain.user.entity.User;
import lang.studyhub.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public Response create(CreateRequest req) {
        if (repo.findByUsername(req.username()).isPresent())
            throw new IllegalArgumentException("이미 존재하는 username");
        if (repo.existsByEmail(req.email()))
            throw new IllegalArgumentException("이미 존재하는 email");

        User u = User.builder()
                .username(req.username())
                .password(encoder.encode(req.password()))
                .nickname(req.nickname())
                .email(req.email())
                .role("USER")
                .build();
        repo.save(u);
        return toResponse(u);
    }

    @Transactional(readOnly = true)
    public Response get(Long id) {
        User u = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        return toResponse(u);
    }

    private Response toResponse(User u) {
        return new Response(u.getId(), u.getUsername(), u.getNickname(), u.getEmail(), u.getRole());
    }
}
