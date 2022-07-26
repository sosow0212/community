package yoon.community.factory;

import yoon.community.entity.board.Board;
import yoon.community.entity.board.Image;

import java.util.List;

import static yoon.community.factory.CategoryFactory.createCategory;
import static yoon.community.factory.UserFactory.createUserWithAdminRole;

public class BoardFactory {
    public static Board createBoardWithImages(List<Image> images) {
        return new Board("title", "content", createUserWithAdminRole(), createCategory(), images);
    }
}
