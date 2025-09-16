package lang.studyhub.domain.board.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lang.studyhub.domain.board.dto.BoardDto;
import lang.studyhub.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시판 관리 API")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public BoardDto.Response create(@Valid @RequestBody BoardDto.CreateRequest req) {
        return boardService.create(req);
    }

    @GetMapping("/{id}")
    public BoardDto.Response get(@PathVariable Long id) {
        return boardService.getById(id);
    }

    @GetMapping("/slug/{slug}")
    public BoardDto.Response getBySlug(@PathVariable String slug) {
        return boardService.getBySlug(slug);
    }

    @PatchMapping("/{id}")
    public BoardDto.Response update(@PathVariable Long id, @Valid @RequestBody BoardDto.UpdateRequest req) {
        return boardService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        boardService.delete(id);
    }
}
