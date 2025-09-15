package lang.studyhub.domain.post.repository;

import lang.studyhub.domain.board.entity.Board;
import lang.studyhub.domain.board.repository.BoardRepository;
import lang.studyhub.domain.post.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PostRepositoryTest {

    @Autowired
    PostRepository postRepo;
    @Autowired
    BoardRepository boardRepo;

    @Test
    void save_and_find_by_board_and_slug() {
        Board b = boardRepo.save(Board.builder().name("공지").slug("notice").visibility("PUBLIC").sortOrder(0).build());
        Post p = postRepo.save(Post.builder().board(b).title("t").slug("hello").content("c").status("PUBLISHED").build());

        assertThat(postRepo.findByBoardIdAndSlug(b.getId(), "hello")).isPresent();
    }
}
