package yoon.community.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import lombok.Setter;
import yoon.community.dto.member.MemberEditRequestDto;
import yoon.community.domain.common.EntityDate;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Member extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(nullable = false)
    private boolean reported;

    @Builder
    public Member(String username, String password, Authority authority) {
        this.username = username;
        this.password = password;
        this.reported = false;
        this.authority = authority;
    }

    public boolean isReported() {
        return this.reported;
    }

    public void editUser(MemberEditRequestDto req) {
        name = req.getName();
        nickname = req.getNickname();
    }

    public void unlockReport() {
        this.reported = false;
    }

    public boolean isSameMemberId(Long id) {
        return Objects.equals(this.id, id);
    }

    public void makeStatusReported() {
        this.reported = true;
    }
}
