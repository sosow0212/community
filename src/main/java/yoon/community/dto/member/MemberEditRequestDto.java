package yoon.community.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.domain.member.Member;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberEditRequestDto {

    private String name;
    private String nickname;

    public static MemberEditRequestDto toDto(Member member) {
        return new MemberEditRequestDto(member.getName(), member.getNickname());
    }
}
