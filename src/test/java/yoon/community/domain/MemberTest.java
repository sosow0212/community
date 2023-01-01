package yoon.community.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static yoon.community.factory.UserFactory.createUser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import yoon.community.dto.member.MemberEditRequestDto;
import yoon.community.entity.member.Member;

public class MemberTest {
    @Test
    @DisplayName("신고처리 테스트")
    public void setStatusIsBeingReportedTest() {
        // given
        Member member = createUser();

        // when
        member.setStatusIsBeingReported();

        // then
        assertThat(member.isReported()).isEqualTo(true);
    }

    @Test
    @DisplayName("정지 해제 테스트")
    public void unlockReportTest() {
        // given
        Member member = createUser();
        member.setStatusIsBeingReported();

        // when
        member.unlockReport();

        // then
        assertThat(member.isReported()).isEqualTo(false);
    }

    @Test
    @DisplayName("유저 수정 테스트")
    public void editUserTest() {
        // given
        Member member = createUser();
        Member editedMember = createUser();
        MemberEditRequestDto req = new MemberEditRequestDto("이름 수정", "닉네임 수정");

        // when
        editedMember.editUser(req);

        // then
        assertThat(member.equals(editedMember)).isEqualTo(false);
    }

    @Test
    @DisplayName("셀프 신고 메서드 테스트")
    public void isReportMySelfTest() {
        // given
        Member member = createUser();

        // when
        boolean result = member.isReportMySelf(member.getId());

        // then
        assertThat(result).isEqualTo(true);
    }
}
