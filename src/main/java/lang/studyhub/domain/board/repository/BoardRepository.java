package lang.studyhub.domain.board.repository;

import lang.studyhub.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
