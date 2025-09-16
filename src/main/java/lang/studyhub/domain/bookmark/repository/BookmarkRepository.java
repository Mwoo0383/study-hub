package lang.studyhub.domain.bookmark.repository;

import lang.studyhub.domain.bookmark.entity.Bookmark;
import lang.studyhub.domain.bookmark.entity.BookmarkId;
import lang.studyhub.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {

    boolean existsByIdUserIdAndIdPostId(Long userId, Long postId);

    Page<Bookmark> findByIdUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    long deleteByIdUserIdAndIdPostId(Long userId, Long postId);

    long countByPost(Post post);
}
