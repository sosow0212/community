package yoon.community.factory;

import yoon.community.entity.comment.Comment;

import static yoon.community.factory.BoardFactory.createBoard;
import static yoon.community.factory.UserFactory.createUser;

public class CommentFactory {

    public static Comment createComment() {
        return new Comment("content", createUser(), createBoard());
    }
}
