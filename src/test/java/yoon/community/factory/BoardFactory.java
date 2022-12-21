package yoon.community.factory;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import yoon.community.entity.board.Board;
import yoon.community.entity.board.Image;

import java.util.ArrayList;
import java.util.List;
import yoon.community.entity.user.User;

import static yoon.community.factory.CategoryFactory.createCategory;
import static yoon.community.factory.ImageFactory.createImage;
import static yoon.community.factory.UserFactory.createUser;
import static yoon.community.factory.UserFactory.createUserWithAdminRole;

public class BoardFactory {
    public static Board createBoardWithImages(List<Image> images) {
        return new Board("title", "content", createUserWithAdminRole(), createCategory(), images);
    }

    public static Board createBoard() {
        List<Image> images = new ArrayList<>();
        images.add(createImage());
        Board board = new Board("title", "content", createUser(), createCategory(), images);
        board.setLiked(0);
        return board;
    }

    public static Board createBoard(User user) {
        List<Image> images = new ArrayList<>();
        images.add(createImage());
        Board board = new Board("title", "content", user, createCategory(), images);
        board.setLiked(0);
        return board;
    }
}
