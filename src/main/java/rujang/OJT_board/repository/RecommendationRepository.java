package rujang.OJT_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rujang.OJT_board.domain.Recommendation;

import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    Optional<Recommendation> findByUserIdAndPostId(Long userId, Long postId);
}
