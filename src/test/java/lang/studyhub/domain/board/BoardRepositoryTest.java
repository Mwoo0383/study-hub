package lang.studyhub.domain.board;

import lang.studyhub.domain.board.entity.Board;
import lang.studyhub.domain.board.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BoardRepositoryTest {

    @Autowired
    BoardRepository repo;

    @Test
    void save_and_find_by_slug() {
        Board b = Board.builder().name("공지").slug("notice").visibility("PUBLIC").sortOrder(0).build();
        repo.save(b);

        assertThat(repo.findBySlug("notice")).isPresent();
    }
}
