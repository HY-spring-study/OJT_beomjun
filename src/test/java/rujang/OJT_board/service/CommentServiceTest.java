package rujang.OJT_board.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rujang.OJT_board.domain.Comment;
import rujang.OJT_board.domain.Post;
import rujang.OJT_board.domain.User;
import rujang.OJT_board.repository.CommentRepository;
import rujang.OJT_board.repository.PostRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    //댓글 생성 성공
    @Test
    void addComment_success() {
        //given
        Long postId = 1L;
        String content = "test comment content";
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("testpassword")
                .build();
        Post post = Post.builder()
                .id(postId)
                .title("test title")
                .content("test content")
                .user(user)
                .build();
        Comment comment = Comment.builder()
                .id(1L)
                .content(content)
                .user(user)
                .post(post)
                .build();

        //게시글이 존재함을 설정
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        //댓글 저장 시, comment 객체를 반환하도록 설정
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        //when
        Comment result = commentService.addComment(postId, content, user);

        //then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(content, result.getContent());
        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    //댓글 생성 시, 게시글이 존재하지 않는 경우 실패
    @Test
    void addComment_fail_postNotFound() {
        //given
        Long postId = 1L;
        String content = "test comment content";
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("testpassword")
                .build();

        // 게시글이 존재하지 않음을 설정
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.addComment(postId, content, user);
        });
        assertEquals("존재하지 않는 게시글입니다.", exception.getMessage());
        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, never()).save(any(Comment.class));
    }
}