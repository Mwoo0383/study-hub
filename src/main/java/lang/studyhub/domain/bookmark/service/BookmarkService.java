package lang.studyhub.domain.bookmark.service;

import jakarta.persistence.EntityNotFoundException;
import lang.studyhub.domain.bookmark.dto.BookmarkCreateRequest;
import lang.studyhub.domain.bookmark.dto.BookmarkExistsResponse;
import lang.studyhub.domain.bookmark.dto.BookmarkResponse;
import lang.studyhub.common.PageResponse;
import lang.studyhub.domain.bookmark.entity.Bookmark;
import lang.studyhub.domain.bookmark.entity.BookmarkId;
import lang.studyhub.domain.bookmark.repository.BookmarkRepository;
import lang.studyhub.domain.post.entity.Post;
import lang.studyhub.domain.post.repository.PostRepository;
import lang.studyhub.domain.user.entity.User;
import lang.studyhub.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookmarkResponse create(Long userId, BookmarkCreateRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        Post post = postRepository.findById(req.postId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found: " + req.postId()));

        BookmarkId id = new BookmarkId(user.getId(), post.getId());
        if (bookmarkRepository.existsById(id)) {
            // 이미 북마크면 그대로 응답(멱등)
            return toResponse(bookmarkRepository.findById(id).orElseThrow());
        }

        Bookmark saved = bookmarkRepository.save(
                Bookmark.builder()
                        .id(id)
                        .user(user)
                        .post(post)
                        .build()
        );
        return toResponse(saved);
    }

    @Transactional
    public void delete(Long userId, Long postId) {
        long deleted = bookmarkRepository.deleteByIdUserIdAndIdPostId(userId, postId);
        if (deleted == 0) {
            // 멱등: 없는 경우에도 에러 없이 통과
        }
    }

    public BookmarkExistsResponse exists(Long userId, Long postId) {
        boolean ok = bookmarkRepository.existsByIdUserIdAndIdPostId(userId, postId);
        return new BookmarkExistsResponse(ok);
    }

    public PageResponse<BookmarkResponse> list(Long userId, Pageable pageable) {
        Page<Bookmark> page = bookmarkRepository.findByIdUserIdOrderByCreatedAtDesc(userId, pageable);
        List<BookmarkResponse> content = page.getContent().stream().map(this::toResponse).toList();
        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    private BookmarkResponse toResponse(Bookmark b) {
        return new BookmarkResponse(
                b.getPost().getId(),
                b.getPost().getTitle(),
                b.getPost().getBoard().getId(),
                b.getUser().getId(),
                b.getCreatedAt()
        );
    }
}
