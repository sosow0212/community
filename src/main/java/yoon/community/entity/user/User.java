package yoon.community.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import lombok.Setter;
import yoon.community.dto.user.UserEditRequestDto;
import yoon.community.entity.common.EntityDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class User extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
    public User(String username, String password, Authority authority) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.reported = false;
        this.authority = authority;
    }

    public boolean isReported() {
        return this.reported;
    }

    public void editUser(UserEditRequestDto req) {
        name = req.getName();
        nickname = req.getNickname();
    }

    public void unlockReport() {
        this.reported = false;
    }

    public boolean isReportMySelf(int id) {
        return this.id == id;
    }

    public void setStatusIsBeingReported() {
        this.reported = true;
    }
}
