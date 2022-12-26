package yoon.community.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static yoon.community.factory.UserFactory.createUser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import yoon.community.dto.user.UserEditRequestDto;
import yoon.community.entity.user.User;

public class UserTest {
    @Test
    @DisplayName("신고처리 테스트")
    public void setStatusIsBeingReportedTest() {
        // given
        User user = createUser();

        // when
        user.setStatusIsBeingReported();

        // then
        assertThat(user.isReported()).isEqualTo(true);
    }

    @Test
    @DisplayName("정지 해제 테스트")
    public void unlockReportTest() {
        // given
        User user = createUser();
        user.setStatusIsBeingReported();

        // when
        user.unlockReport();

        // then
        assertThat(user.isReported()).isEqualTo(false);
    }

    @Test
    @DisplayName("유저 수정 테스트")
    public void editUserTest() {
        // given
        User user = createUser();
        User editedUser = createUser();
        UserEditRequestDto req = new UserEditRequestDto("이름 수정", "닉네임 수정");

        // when
        editedUser.editUser(req);

        // then
        assertThat(user.equals(editedUser)).isEqualTo(false);
    }

    @Test
    @DisplayName("셀프 신고 메서드 테스트")
    public void isReportMySelfTest() {
        // given
        User user = createUser();

        // when
        boolean result = user.isReportMySelf(user.getId());

        // then
        assertThat(result).isEqualTo(true);
    }
}
