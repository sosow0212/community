package yoon.community.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static yoon.community.factory.CommentFactory.createComment;
import static yoon.community.factory.UserFactory.createUser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import yoon.community.domain.comment.Comment;
import yoon.community.domain.member.Member;

public class CommentTest {
    @Test
    @DisplayName("자신의 댓글인지 확인하는 메서드 테스트")
    public void isOwnCommentTest() {
        // given
        Member member = createUser();
        Comment comment = createComment(member);

        // when
        boolean result = comment.isOwnComment(member);

        // then
        assertThat(result).isEqualTo(true);
    }
}
