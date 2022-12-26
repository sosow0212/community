package yoon.community.factory.dto;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import yoon.community.dto.board.BoardUpdateRequest;

public class BoardUpdateRequestFactory {
    public static BoardUpdateRequest createBoardUpdateRequest(String title, String content, List<MultipartFile> addedImage, List<Integer> deletedImage) {
        return new BoardUpdateRequest(title, content, addedImage, deletedImage);
    }
}
