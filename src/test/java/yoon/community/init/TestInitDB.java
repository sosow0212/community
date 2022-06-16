package yoon.community.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.entity.user.Authority;
import yoon.community.entity.user.User;
import yoon.community.repository.UserRepository;

import java.util.List;

@Component
public class TestInitDB {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private String userUsername = "user_username";
    private String userUsername2 = "user2_username";
    private String adminUsername = "admin_username";
    private String password = "1234";

    @Transactional
    public void initDB() {
        initTestUser();
        initTestAdmin();
    }

    private void initTestAdmin() {
        userRepository.saveAll(
                List.of(
                        new User(1, adminUsername, passwordEncoder.encode(password), "이름", "닉네임", Authority.ROLE_ADMIN)
                ));

    }

    private void initTestUser() {
        userRepository.saveAll(
                List.of(
                        new User(1, userUsername, passwordEncoder.encode(password), "이름", "닉네임", Authority.ROLE_USER),
                        new User(2, userUsername, passwordEncoder.encode(password), "이름2", "닉네임2", Authority.ROLE_USER))
                );
    }

    public String getUserUsername() {
        return userUsername;
    }

    public String getUserUsername2() {
        return userUsername2;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public String getPassword() {
        return password;
    }
}
