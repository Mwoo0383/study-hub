package lang.studyhub.domain.comment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lang.studyhub.domain.comment.dto.CommentDto.*;
import lang.studyhub.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name="Comment", description="댓글 API")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public Response create(@Valid @RequestBody CreateRequest req) {
        return commentService.create(req);
    }

    @GetMapping("/posts/{postId}")
    public Page<Response> list(@PathVariable Long postId, Pageable pageable) {
        return commentService.list(postId, pageable);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }
}
