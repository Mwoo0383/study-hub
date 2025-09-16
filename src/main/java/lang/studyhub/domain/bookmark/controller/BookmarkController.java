package lang.studyhub.domain.bookmark.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lang.studyhub.domain.bookmark.dto.BookmarkCreateRequest;
import lang.studyhub.domain.bookmark.dto.BookmarkExistsResponse;
import lang.studyhub.domain.bookmark.dto.BookmarkResponse;
import lang.studyhub.common.PageResponse;
import lang.studyhub.domain.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
@Tag(name = "Bookmarks", description = "북마크 API")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // 임시로 헤더에서 사용자 식별 (추후 Security로 대체)
    private Long currentUserId(String header) {
        if (header == null || header.isBlank()) throw new IllegalArgumentException("X-USER-ID header required");
        return Long.parseLong(header);
    }

    @PostMapping
    public ResponseEntity<BookmarkResponse> create(
            @RequestHeader(name = "X-USER-ID", required = true) String userIdHeader,
            @Valid @RequestBody BookmarkCreateRequest request
    ) {
        Long userId = currentUserId(userIdHeader);
        return ResponseEntity.ok(bookmarkService.create(userId, request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(
            @RequestHeader(name = "X-USER-ID", required = true) String userIdHeader,
            @PathVariable Long postId
    ) {
        Long userId = currentUserId(userIdHeader);
        bookmarkService.delete(userId, postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<BookmarkExistsResponse> exists(
            @RequestHeader(name = "X-USER-ID", required = true) String userIdHeader,
            @PathVariable Long postId
    ) {
        Long userId = currentUserId(userIdHeader);
        return ResponseEntity.ok(bookmarkService.exists(userId, postId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookmarkResponse>> list(
            @RequestHeader(name = "X-USER-ID", required = true) String userIdHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Long userId = currentUserId(userIdHeader);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(bookmarkService.list(userId, pageable));
    }
}
