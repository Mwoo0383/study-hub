package lang.studyhub.domain.post.repository;

import lang.studyhub.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByBoardIdAndSlug(Long boardId, String slug);
    Page<Post> findByBoardIdAndStatusOrderByCreatedAtDesc(Long boardId, String status, Pageable pageable);
}
