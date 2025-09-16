package lang.studyhub.domain.like.repository;

import lang.studyhub.domain.like.entity.PostLike;
import lang.studyhub.domain.like.id.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    boolean existsByUser_IdAndPost_Id(Long userId, Long postId);
    long countByPost_Id(Long postId);
    void deleteByUser_IdAndPost_Id(Long userId, Long postId);
}
