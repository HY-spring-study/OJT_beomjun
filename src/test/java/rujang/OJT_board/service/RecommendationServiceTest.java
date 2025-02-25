package rujang.OJT_board.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rujang.OJT_board.domain.Post;
import rujang.OJT_board.domain.Recommendation;
import rujang.OJT_board.domain.User;
import rujang.OJT_board.repository.PostRepository;
import rujang.OJT_board.repository.RecommendationRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private RecommendationService recommendationService;

    //추천 성공
    @Test
    void recommendation_add_success() {
        //given
        Long postId = 1L;
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("testpassword")
                .build();
        Post post = Post.builder()
                .id(postId)
                .title("title")
                .content("content")
                .user(user)
                .build();

        //추천내역이 없음을 설정
        when(recommendationRepository.findByUserIdAndPostId(user.getId(), postId))
                .thenReturn(Optional.empty());

        //게시글이 존재함을 설정
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        //when
        recommendationService.toggleRecommendation(postId, user);

        //then : 추천 내역이 없었으므로 save가 됨
        verify(recommendationRepository, times(1)).save(any(Recommendation.class));
        verify(recommendationRepository, never()).delete(any(Recommendation.class));
    }

    //추천 취소 성공
    @Test
    void recommendation_remove_success() {
        //given
        Long postId = 1L;
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("testpassword")
                .build();
        Post post = Post.builder()
                .id(postId)
                .title("title")
                .content("content")
                .user(user)
                .build();
        Recommendation recommendation = Recommendation.builder()
                .id(1L)
                .user(user)
                .post(post)
                .build();

        //추천 내용이 이미 있음을 설정
        when(recommendationRepository.findByUserIdAndPostId(user.getId(), postId))
                .thenReturn(Optional.of(recommendation));
        
        ////게시글이 존재함을 설정
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        //when
        recommendationService.toggleRecommendation(postId, user);

        //then : 추천내역이 있었으므로 delete가 됨
        verify(recommendationRepository, times(1)).delete(recommendation);
        verify(recommendationRepository, never()).save(any(Recommendation.class));
    }

    //추천 추가 시, 게시글이 존재하지 않은 경우 실패
    @Test
    void recommendation_nonExistentPost() {
        //given
        Long postId = 1L;
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("testpassword")
                .build();

        //게시글이 없음을 설정
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        //when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            recommendationService.toggleRecommendation(postId, user);
        });
        assertEquals("존재하지 않는 게시글입니다.", exception.getMessage());
        verify(recommendationRepository, never()).save(any(Recommendation.class));
    }
}