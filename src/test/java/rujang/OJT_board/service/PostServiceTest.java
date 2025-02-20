package rujang.OJT_board.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rujang.OJT_board.domain.Post;
import rujang.OJT_board.domain.User;
import rujang.OJT_board.repository.PostRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    //게시물 생성 성공
    void createPost_success() {
        //given
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
}