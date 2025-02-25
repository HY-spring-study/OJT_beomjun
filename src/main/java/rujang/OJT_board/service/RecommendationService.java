package rujang.OJT_board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rujang.OJT_board.domain.Post;
import rujang.OJT_board.domain.Recommendation;
import rujang.OJT_board.domain.User;
import rujang.OJT_board.repository.PostRepository;
import rujang.OJT_board.repository.RecommendationRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final PostRepository postRepository;

    /**
     * 게시글 추천 기능
     * 유저가 이미 추천을 했으면 추천 취소
     * 아니면 추천
     */
    @Transactional
    public void toggleRecommendation(Long postId, User user) {
        //게시글 존재 여부 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        //해당 유저와 게시글에 대한 추천 내역 조회
        Optional<Recommendation> optionalRec = recommendationRepository.findByUserIdAndPostId(user.getId(), postId);

        if(optionalRec.isPresent()){
            //이미 추천을 했으면 추천 취소
            recommendationRepository.delete(optionalRec.get());
        } else {
            //아니면 추천
            Recommendation recommendation = Recommendation.builder()
                    .user(user)
                    .post(post)
                    .build();
            recommendationRepository.save(recommendation);
        }
    }
}
