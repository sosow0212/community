package yoon.community.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndPassword(String username, String password);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByUsername(String username);
    public boolean existsByUsername(String username);
    public boolean existsByNickname(String nickname);

    List<User> findByReportedIsTrue();
}
