package rujang.OJT_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rujang.OJT_board.domain.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * 추천 수 기준으로 게시글을 내림차순 정렬하여 조회
     * - LEFT JOIN으로 추천이 없는 게시글도 함께 조회
     * - COUNT(r.id)로 추천 개수를 집계
     */
    @Query("SELECT p FROM Post p " +
            "LEFT JOIN p.recommendations r " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(r.id) DESC")
    List<Post> findAllOrderByRecommendationCountDesc();

    //생성시간을 기준으로 게시글 내림차순 정렬 (Jpa에서 제공)
    List<Post> findAllByOrderByCreatedAtDesc();
}
