package rujang.OJT_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rujang.OJT_board.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
