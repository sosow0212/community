package yoon.community.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.entity.user.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditRequestDto {
    private String name; // 유저 실명
    private String nickname; // 유저 닉네임

    public static UserEditRequestDto toDto(User user) {
        return new UserEditRequestDto(user.getName(), user.getNickname());
    }

}
