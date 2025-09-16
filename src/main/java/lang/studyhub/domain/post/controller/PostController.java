package lang.studyhub.domain.post.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lang.studyhub.domain.post.dto.PostDto.*;
import lang.studyhub.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 CRUD API")
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response create(@Valid @RequestBody CreateRequest req) {
        return postService.create(req);
    }

    @GetMapping("/{id}")
    public Response get(@PathVariable Long id) {
        return postService.get(id);
    }

    @GetMapping("/boards/{boardId}/slug/{slug}")
    public Response getBySlug(@PathVariable Long boardId, @PathVariable String slug) {
        return postService.getBySlug(boardId, slug);
    }

    @GetMapping("/boards/{boardId}")
    public Page<Response> list(@PathVariable Long boardId,
                               @RequestParam(required = false) String status,
                               Pageable pageable) {
        return postService.list(boardId, status, pageable);
    }

    @PatchMapping("/{id}")
    public Response update(@PathVariable Long id, @Valid @RequestBody UpdateRequest req) {
        return postService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }

    @PostMapping("/{id}/view")
    public void increaseView(@PathVariable Long id) {
        postService.increaseView(id);
    }
}
