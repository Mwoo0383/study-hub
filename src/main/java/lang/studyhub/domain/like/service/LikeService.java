package lang.studyhub.domain.like.service;

import lang.studyhub.domain.like.dto.LikeDto.*;
import lang.studyhub.domain.like.entity.PostLike;
import lang.studyhub.domain.like.id.PostLikeId;
import lang.studyhub.domain.like.repository.PostLikeRepository;
import lang.studyhub.domain.post.entity.Post;
import lang.studyhub.domain.post.repository.PostRepository;
import lang.studyhub.domain.user.entity.User;
import lang.studyhub.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
@Transactional
public class LikeService {

    private final PostLikeRepository repo;
    private final UserRepository userRepo;
    private final PostRepository postRepo;

    public Response toggle(ToggleRequest req) {
        Long uid = req.userId();
        Long pid = req.postId();

        if (repo.existsByUser_IdAndPost_Id(uid, pid)) {
            repo.deleteByUser_IdAndPost_Id(uid, pid);
        } else {
            User u = userRepo.findById(uid).orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
            Post p = postRepo.findById(pid).orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

            PostLike like = PostLike.builder()
                    .id(new PostLikeId(uid, pid))
                    .user(u)
                    .post(p)
                    .build();
            repo.save(like);
        }
        long count = repo.countByPost_Id(pid);
        boolean liked = repo.existsByUser_IdAndPost_Id(uid, pid);
        return new Response(pid, count, liked);
    }

    @Transactional(readOnly = true)
    public long count(Long postId) { return repo.countByPost_Id(postId); }
}
