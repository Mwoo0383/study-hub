package lang.studyhub.domain.like.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lang.studyhub.domain.like.dto.LikeDto.*;
import lang.studyhub.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
@Tag(name="Like", description="좋아요 API")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/toggle")
    public Response toggle(@Valid @RequestBody ToggleRequest req) {
        return likeService.toggle(req);
    }

    @GetMapping("/posts/{postId}/count")
    public long count(@PathVariable Long postId) {
        return likeService.count(postId);
    }
}
