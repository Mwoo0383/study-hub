package lang.studyhub.domain.board.service;


import lang.studyhub.domain.board.dto.BoardDto;
import lang.studyhub.domain.board.entity.Board;
import lang.studyhub.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository repo;

    public BoardDto.Response create(BoardDto.CreateRequest req) {
        if (repo.existsBySlug(req.slug()))
            throw new IllegalArgumentException("이미 존재하는 slug 입니다.");

        Board b = Board.builder()
                .name(req.name())
                .slug(req.slug())
                .description(req.description())
                .visibility(req.visibility() == null ? "PUBLIC" : req.visibility())
                .sortOrder(req.sortOrder() == null ? 0 : req.sortOrder())
                .build();
        repo.save(b);
        return toResponse(b);
    }

    @Transactional(readOnly = true)
    public BoardDto.Response getById(Long id) {
        Board b = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("게시판 없음"));
        return toResponse(b);
    }

    @Transactional(readOnly = true)
    public BoardDto.Response getBySlug(String slug) {
        Board b = repo.findBySlug(slug).orElseThrow(() -> new IllegalArgumentException("게시판 없음"));
        return toResponse(b);
    }

    public BoardDto.Response update(Long id, BoardDto.UpdateRequest req) {
        Board b = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("게시판 없음"));
        if (req.name() != null) b.setName(req.name());
        if (req.slug() != null) {
            if (!b.getSlug().equals(req.slug()) && repo.existsBySlug(req.slug()))
                throw new IllegalArgumentException("이미 존재하는 slug 입니다.");
            b.setSlug(req.slug());
        }
        if (req.description() != null) b.setDescription(req.description());
        if (req.visibility() != null) b.setVisibility(req.visibility());
        if (req.sortOrder() != null) b.setSortOrder(req.sortOrder());
        return toResponse(b);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    private BoardDto.Response toResponse(Board b) {
        return new BoardDto.Response(b.getId(), b.getName(), b.getSlug(), b.getDescription(),
                b.getVisibility(), b.getSortOrder());
    }
}
