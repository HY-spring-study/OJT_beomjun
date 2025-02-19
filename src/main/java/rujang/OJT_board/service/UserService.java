package rujang.OJT_board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rujang.OJT_board.domain.User;
import rujang.OJT_board.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User register(String username,String password){
        //이미 등록된 사용자인지 확인
        if(userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        //유저 엔티티 생성 및 저장
        User user = User.builder()
                .username(username)
                .password(password)
                .build();

        return userRepository.save(user);
    }
}
