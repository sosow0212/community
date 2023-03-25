package yoon.community.factory;

import static yoon.community.factory.CategoryFactory.createCategory;
import static yoon.community.factory.ImageFactory.createImage;
import static yoon.community.factory.UserFactory.createUser;
import static yoon.community.factory.UserFactory.createUserWithAdminRole;

import java.util.ArrayList;
import java.util.List;
import yoon.community.domain.board.Board;
import yoon.community.domain.board.Image;
import yoon.community.domain.category.Category;
import yoon.community.domain.member.Member;

public class BoardFactory {

    public static Board createBoardWithImages(List<Image> images) {
        return new Board("title", "content", createUserWithAdminRole(), createCategory(), images);
    }

    public static Board createBoardWithImages(Member member, Category category, List<Image> images) {
        return new Board("title", "content", member, category, images);
    }


    public static Board createBoard() {
        List<Image> images = new ArrayList<>();
        images.add(createImage());
        Board board = new Board("title", "content", createUser(), createCategory(), images);
        board.setLiked(0);
        return board;
    }

    public static Board createBoard(Member member) {
        List<Image> images = new ArrayList<>();
        images.add(createImage());
        Board board = new Board("title", "content", member, createCategory(), images);
        board.setLiked(0);
        return board;
    }
}
