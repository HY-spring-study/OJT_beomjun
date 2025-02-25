package rujang.OJT_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rujang.OJT_board.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
