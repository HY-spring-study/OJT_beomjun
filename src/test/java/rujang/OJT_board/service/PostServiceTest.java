package rujang.OJT_board.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rujang.OJT_board.domain.Post;
import rujang.OJT_board.domain.User;
import rujang.OJT_board.dto.PostUpdateDTO;
import rujang.OJT_board.repository.PostRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    //게시물 생성 성공
    @Test
    void createPost_success() {
        //given : 작성자와 게시물 객체 (저장 전)
        User user = User.builder()
                .id(1L)
                .username("testname")
                .password("testpassword")
                .build();

        Post post = Post.builder()
                .title("testtitle")
                .content("testcontent")
                .user(user)
                .build();
        
        //저장 후 반환될 게시물 객체
        Post savedPost = Post.builder()
                .id(1L)
                .title("testtitle")
                .content("testcontent")
                .user(user)
                .build();

        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        //when
        Post result = postService.createPost(post);

        //then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testtitle", result.getTitle());
        assertEquals("testcontent", result.getContent());
        assertEquals(user, result.getUser());

        verify(postRepository, times(1)).save(any(Post.class));
    }

    //게시물 수정 성공
    @Test
    void updatePost_success() {
        //given : 작성자와 기존 게시물, 수정 요청을 담은 DTO 준비
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("testpassword")
                .build();
        Post originalPost = Post.builder()
                .id(1L)
                .title("Old title")
                .content("Old content")
                .user(user)
                .build();
        PostUpdateDTO updateDTO = new PostUpdateDTO();
        updateDTO.setTitle("New title");
        updateDTO.setContent("New content");

        when(postRepository.findById(1L)).thenReturn(Optional.of(originalPost));
        //update하면 originalPost의 내부 필드를 변경하므로, 저장 후 원래 객체를 반환하도록 설정
        when(postRepository.save(originalPost)).thenReturn(
                Post.builder()
                        .id(1L)
                        .title("New title")
                        .content("New content")
                        .user(user)
                        .build()
        );

        //when : 게시물 수정 메서드 호출
        Post result = postService.updatePost(1L, updateDTO, user);

        //then : 게시물의 제목과 내용이 수정되었는지 검증
        assertNotNull(result);
        assertEquals("New title", result.getTitle());
        assertEquals("New content", result.getContent());
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(originalPost);
    }

    //게시물 수정 시 잘못된 사용자가 수정 요청하는 경우
    @Test
    void updatePost_fail_wrongUser() {
        //given : 작성자와 다른 사용자를 준비, 수정 요청을 담은 DTO 준비
        User author = User.builder()
                .id(1L)
                .username("testuser")
                .password("testpassword")
                .build();
        User wrongUser = User.builder()
                .id(2L)
                .username("wronguser")
                .password("wrongpassword")
                .build();
        Post post = Post.builder()
                .id(1L)
                .title("Old title")
                .content("Old content")
                .user(author)
                .build();
        PostUpdateDTO updateDTO = new PostUpdateDTO();
        updateDTO.setTitle("New title");
        updateDTO.setContent("New content");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        //when & then : 수정 권한이 없는 사용자로 호출 시 예외 발생 확인
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.updatePost(1L, updateDTO, wrongUser);
        });
        assertEquals("수정 권한이 없습니다.", exception.getMessage());
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, never()).save(any(Post.class));
    }

    //게시물 수정 시 존재하지 않는 게시물 예외 테스트
    @Test
    void updatePost_fail_nonExistentPost() {
        //given : 사용자와 수정 요청 DTO 준비
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("testpassword")
                .build();
        PostUpdateDTO updateDTO = new PostUpdateDTO();
        updateDTO.setTitle("New title");
        updateDTO.setContent("New content");

        //해당 게시물이 존재하지 않음을 가정
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        //when & then : 게시물이 존재하지 않을 때 예외가 발생하는지 검증
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.updatePost(1L, updateDTO, user);
        });
        assertEquals("존재하지 않는 게시물입니다.", exception.getMessage());
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, never()).save(any(Post.class));
    }

    //게시물 삭제 성공 테스트
    @Test
    void deletePost_success() {
        //given : 삭제할 게시물과 작성자 준비
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("testpassword")
                .build();
        Post post = Post.builder()
                .id(1L)
                .title("title")
                .content("content")
                .user(user)
                .build();
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        //when : 게시물 삭제 메서드 호출
        postService.deletePost(1L, user);

        //then : 게시물 조회 및 삭제가 정살적으로 호출되었는지 검증
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).delete(post);
    }

    //게시물 삭제 시 잘못된 사용자가 삭제 요청하는 경우
    @Test
    void deletePost_fail_wrongUser() {
        //given : 작성자와 다른 사용자, 삭제할 게시물 준비
        User author = User.builder()
                .id(1L)
                .username("testuser")
                .password("testpassword")
                .build();
        User wrongUser = User.builder()
                .id(2L)
                .username("wronguser")
                .password("wrongpassword")
                .build();
        Post post = Post.builder()
                .id(1L)
                .title("title")
                .content("content")
                .user(author)
                .build();
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        //when & then : 삭제 권한이 없는 사용자로 호출 시 예외 발생 활인
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.deletePost(1L, wrongUser);
        });
        assertEquals("삭제 권한이 없습니다.", exception.getMessage());
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, never()).delete(any(Post.class));
    }

    //게시물 삭제 시 존재하지 않는 게시물에 대한 예외 테스트
    @Test
    void deletePost_fail_nonExistentPost() {
        //given : 삭제할 게시물이 존재하지 않음을 가정
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("testpassword")
                .build();
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        //when & then : 존재하지 않는 게시물 삭제 시 예외가 발생하는지 검즘
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.deletePost(1L, user);
        });
        assertEquals("존재하지 않는 게시물입니다.", exception.getMessage());
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, never()).delete(any(Post.class));
    }
}