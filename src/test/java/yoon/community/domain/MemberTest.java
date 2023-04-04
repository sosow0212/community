package yoon.community.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static yoon.community.factory.UserFactory.createUser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import yoon.community.domain.member.Member;
import yoon.community.dto.member.MemberEditRequestDto;

public class MemberTest {

    @Test
    @DisplayName("유저를 신고처리 한다.")
    public void make_status_reported_success() {
        // given
        Member member = createUser();

        // when
        member.makeStatusReported();

        // then
        assertThat(member.isReported()).isEqualTo(true);
    }

    @Test
    @DisplayName("유저의 신고 상태를 해제한다.")
    public void unlock_reported_user_success() {
        // given
        Member member = createUser();
        member.makeStatusReported();

        // when
        member.unlockReport();

        // then
        assertThat(member.isReported()).isEqualTo(false);
    }

    @Test
    @DisplayName("유저의 정보를 수정한다.")
    public void edit_user_success() {
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
    @DisplayName("같은 Member Id인지 확인한다.")
    public void check_same_member_success() {
        // given
        Member member = createUser();

        // when
        boolean result = member.isSameMemberId(member.getId());

        // then
        assertThat(result).isEqualTo(true);
    }
}
