package yoon.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
    public User findByName(String name);
}
