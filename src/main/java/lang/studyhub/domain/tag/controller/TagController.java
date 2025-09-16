package lang.studyhub.domain.tag.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lang.studyhub.domain.tag.dto.TagDto.*;
import lang.studyhub.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
@Tag(name="Tag", description="태그 API")
public class TagController {
    private final TagService tagService;

    @PostMapping
    public Response create(@Valid @RequestBody CreateRequest req) {
        return tagService.create(req);
    }

    @GetMapping
    public List<Response> list() {
        return tagService.list();
    }
}
