package yoon.community.factory;

import yoon.community.domain.member.Authority;
import yoon.community.domain.member.Member;

public class UserFactory {

    public static Member createUserWithAdminRole() {
        Member member = new Member("username", "password", Authority.ROLE_ADMIN);
        member.setName("yoon");
        member.setNickname("yoon");
        return member;
    }

    public static Member createUser() {
        Member member = new Member("username", "password", Authority.ROLE_ADMIN);
        member.setName("yoon");
        member.setNickname("yoon");
        return member;
    }

    public static Member createUser(String username, String password) {
        Member member = new Member(username, password, Authority.ROLE_ADMIN);
        member.setName("yoon");
        member.setNickname("yoon");
        return member;
    }

    public static Member createUser2() {
        Member member = new Member("username2", "password2", Authority.ROLE_ADMIN);
        member.setName("yoon2");
        member.setNickname("yoon2");
        return member;
    }
}
