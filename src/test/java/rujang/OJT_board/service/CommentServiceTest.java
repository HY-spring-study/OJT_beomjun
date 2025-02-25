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

    //댓글 삭제 성공
    @Test
    void deleteComment_success() {
        // given: 댓글 작성자와 삭제할 댓글 준비
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("testpassword")
                .build();
        Comment comment = Comment.builder()
                .id(1L)
                .content("testcomment")
                .user(user)
                .build();
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // when : 댓글 삭제 메서드 호출
        commentService.deleteComment(1L, user);

        // then : 댓글이 정상 삭제되었음을 확인
        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).delete(comment);
    }

    //댓글 삭제 시, 잘못된 사용자가 삭제 요청하는 경우 실패
    @Test
    void deleteComment_fail_wrongUser() {
        //given: 댓글 작성자와 다른 사용자를 준비
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("testpassword")
                .build();
        User wrongUser = User.builder()
                .id(2L)
                .username("wronguser")
                .password("wrongpassword")
                .build();
        Comment comment = Comment.builder()
                .id(1L)
                .content("testcomment")
                .user(user)
                .build();
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        //when & then : 삭제 권한이 없는 사용자가 삭제 시도하면 예외 발생 확인
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.deleteComment(1L, wrongUser);
        });
        assertEquals("삭제 권한이 없습니다.", exception.getMessage());
        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, never()).delete(any(Comment.class));
    }

    //댓글 삭제 시, 존재하지 않는 댓글이라면 실패
    @Test
    void deleteComment_fail_nonExistentComment() {
        // given: 삭제할 댓글이 존재하지 않음을 설정
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .build();
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        //when & then: 존재하지 않는 댓글 삭제 요청 시 실패
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.deleteComment(1L, user);
        });
        assertEquals("존재하지 않는 댓글입니다.", exception.getMessage());
        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, never()).delete(any(Comment.class));
    }
}