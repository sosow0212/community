package yoon.community.factory;

import static yoon.community.factory.BoardFactory.createBoard;
import static yoon.community.factory.UserFactory.createUser;

import yoon.community.domain.comment.Comment;
import yoon.community.domain.member.Member;

public class CommentFactory {

    public static Comment createComment() {
        return new Comment("content", createUser(), createBoard());
    }

    public static Comment createComment(Member member) {
        return new Comment("content", member, createBoard());
    }
}
