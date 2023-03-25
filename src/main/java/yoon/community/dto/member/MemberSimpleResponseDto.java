package yoon.community.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.domain.member.Member;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberSimpleResponseDto {

    private String username;
    private String name;

    public static MemberSimpleResponseDto toDto(Member member) {
        return new MemberSimpleResponseDto(member.getUsername(), member.getName());
    }
}
