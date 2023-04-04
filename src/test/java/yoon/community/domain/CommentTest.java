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
    @DisplayName("자신의 댓글인지 확인한다.")
    public void check_comment_is_mine() {
        // given
        Member member = createUser();
        Comment comment = createComment(member);

        // when
        boolean result = comment.isOwnComment(member);

        // then
        assertThat(result).isEqualTo(true);
    }
}
