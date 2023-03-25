package yoon.community.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.domain.member.Member;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberSimpleNicknameResponseDto {

    private String name;
    private String nickname;

    public static MemberSimpleNicknameResponseDto toDto(Member member) {
        return new MemberSimpleNicknameResponseDto(member.getName(), member.getNickname());
    }
}
