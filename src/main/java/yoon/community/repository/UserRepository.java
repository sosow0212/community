package yoon.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsernameAndPassword(String username, String password);
    Optional<User> findByUsername(String username);
    public boolean existsByUsername(String username);
    public boolean existsByNickname(String nickname);
}
