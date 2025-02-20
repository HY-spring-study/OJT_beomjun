package rujang.OJT_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rujang.OJT_board.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
