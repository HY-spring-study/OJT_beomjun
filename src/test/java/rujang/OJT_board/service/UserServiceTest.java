package rujang.OJT_board.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rujang.OJT_board.domain.User;
import rujang.OJT_board.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    //회원가입성공
    void register_success() {
        //given
        String username = "testuser";
        String password = "testpassword";

        //존재하지 않는 사용자임을 가정
        when(userRepository.existsByUsername(username)).thenReturn(false);

        //저장 시 리턴할 유저 엔티티를 미리 설정 (ID 할당)
        User savedUser = User.builder()
                .id(1L)
                .username(username)
                .password(password)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        //when
        User result = userService.register(username, password);

        //then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());

        verify(userRepository, times(1)).existsByUsername(username);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    //중복 회원 발생
    void register_fail() {
        //given
        String username = "testuser";
        String password = "testpassword";

        //이미 존재한다고 가정
        when(userRepository.existsByUsername(username)).thenReturn(true);

        //when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(username, password);
        });
        assertEquals("이미 존재하는 사용자입니다.", exception.getMessage());

        verify(userRepository, times(1)).existsByUsername(username);
        verify(userRepository, never()).save(any(User.class));
    }
}