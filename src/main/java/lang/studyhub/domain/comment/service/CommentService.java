package lang.studyhub.domain.comment.service;

import lang.studyhub.domain.comment.dto.CommentDto.*;
import lang.studyhub.domain.comment.entity.Comment;
import lang.studyhub.domain.comment.repository.CommentRepository;
import lang.studyhub.domain.post.entity.Post;
import lang.studyhub.domain.post.repository.PostRepository;
import lang.studyhub.domain.user.entity.User;
import lang.studyhub.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;

    public Response create(CreateRequest req) {
        Post post = postRepo.findById(req.postId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        User user = userRepo.findById(req.userId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        Comment c = Comment.builder()
                .post(post)
                .user(user)
                .content(req.content())
                .build();
        commentRepo.save(c);
        return toResponse(c);
    }

    @Transactional(readOnly = true)
    public Page<Response> list(Long postId, Pageable pageable) {
        return commentRepo.findByPostIdOrderByCreatedAtDesc(postId, pageable)
                .map(this::toResponse);
    }

    public void delete(Long id) {
        commentRepo.deleteById(id);
    }

    private Response toResponse(Comment c) {
        return new Response(
                c.getId(),
                c.getPost().getId(),
                c.getUser().getId(),
                c.getContent(),
                c.getCreatedAt().toString(),
                c.getUpdatedAt().toString()
        );
    }
}
