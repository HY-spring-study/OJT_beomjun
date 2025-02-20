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

    @Transactional
    public User login(String username,String password){
        //username으로 유저 조회, 없으면 예외발생
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        //비밀번호 일치 여부 확인
        if(!user.getPassword().equals(password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }
}
