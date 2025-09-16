package lang.studyhub.domain.post.service;

import lang.studyhub.domain.board.entity.Board;
import lang.studyhub.domain.board.repository.BoardRepository;
import lang.studyhub.domain.post.dto.PostDto.*;
import lang.studyhub.domain.post.entity.Post;
import lang.studyhub.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepo;
    private final BoardRepository boardRepo;

    public Response create(CreateRequest req) {
        Board board = boardRepo.findById(req.boardId())
                .orElseThrow(() -> new IllegalArgumentException("게시판 없음"));

        postRepo.findByBoardIdAndSlug(board.getId(), req.slug()).ifPresent(p -> {
            throw new IllegalArgumentException("이미 존재하는 slug 입니다.");
        });

        Post p = Post.builder()
                .board(board)
                .title(req.title())
                .slug(req.slug())
                .content(req.content())
                .status(req.status() == null ? "PUBLISHED" : req.status())
                .build();
        postRepo.save(p);
        return toResponse(p);
    }

    @Transactional(readOnly = true)
    public Response get(Long id) {
        Post p = postRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        return toResponse(p);
    }

    @Transactional(readOnly = true)
    public Response getBySlug(Long boardId, String slug) {
        Post p = postRepo.findByBoardIdAndSlug(boardId, slug)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        return toResponse(p);
    }

    @Transactional(readOnly = true)
    public Page<Response> list(Long boardId, String status, Pageable pageable) {
        return postRepo.findByBoardIdAndStatusOrderByCreatedAtDesc(
                boardId, status == null ? "PUBLISHED" : status, pageable
        ).map(this::toResponse);
    }

    public Response update(Long id, UpdateRequest req) {
        Post p = postRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        if (req.title() != null) p.setTitle(req.title());
        if (req.slug() != null) {
            postRepo.findByBoardIdAndSlug(p.getBoard().getId(), req.slug()).ifPresent(other -> {
                if (!other.getId().equals(p.getId()))
                    throw new IllegalArgumentException("이미 존재하는 slug 입니다.");
            });
            p.setSlug(req.slug());
        }
        if (req.content() != null) p.setContent(req.content());
        if (req.status() != null) p.setStatus(req.status());
        return toResponse(p);
    }

    public void delete(Long id) { postRepo.deleteById(id); }

    public void increaseView(Long id) {
        Post p = postRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        p.setViewCount(p.getViewCount() + 1);
    }

    private Response toResponse(Post p) {
        return new Response(
                p.getId(),
                p.getBoard().getId(),
                p.getTitle(),
                p.getSlug(),
                p.getContent(),
                p.getStatus(),
                p.getViewCount(),
                p.getCreatedAt().toString(),
                p.getUpdatedAt().toString()
        );
    }
}
