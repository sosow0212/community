package yoon.community.factory;

import yoon.community.entity.user.Authority;
import yoon.community.entity.user.User;

public class UserFactory {
    public static User createUserWithAdminRole() {
        User user = new User("username", "password", Authority.ROLE_ADMIN);
        user.setName("yoon");
        user.setNickname("yoon");
        return user;
    }
}
