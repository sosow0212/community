package yoon.community.domain;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static yoon.community.factory.BoardFactory.createBoard;
import static yoon.community.factory.BoardFactory.createBoardWithImages;
import static yoon.community.factory.CategoryFactory.createCategory;
import static yoon.community.factory.ImageFactory.createImage;
import static yoon.community.factory.ImageFactory.createImageWithIdAndOriginName;
import static yoon.community.factory.UserFactory.createUser;
import static yoon.community.factory.dto.BoardUpdateRequestFactory.createBoardUpdateRequest;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import yoon.community.dto.board.BoardUpdateRequest;
import yoon.community.entity.board.Board;
import yoon.community.entity.board.Board.ImageUpdatedResult;
import yoon.community.entity.board.Image;

public class BoardTest {
    @Test
    @DisplayName("신고 테스트")
    public void setStatusIsBeingReportedTest() {
        // given
        Board board = createBoard();
        board.setStatusIsBeingReported();

        // when
        boolean result = board.isReported();

        // then
        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("좋아요, 즐겨찾기 테스트")
    public void increaseLikeCountTest() {
        // given
        Board board = createBoard();

        // when
        board.increaseLikeCount();
        board.increaseFavoriteCount();

        // then
        assertThat(board.getLiked()).isEqualTo(1);
        assertThat(board.getFavorited()).isEqualTo(1);
    }

    @Test
    @DisplayName("신고 해제 테스트")
    public void unReportedTest() {
        // given
        Board board = createBoard();
        board.setStatusIsBeingReported();

        // when
        board.unReportedBoard();
        boolean result = board.isReported();

        // then
        assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("editBoard 테스트")
    public void updateTest() {
        // given
        Image a = createImage();
        Image b = createImage();
        Board board = createBoardWithImages(createUser(), createCategory(), List.of(a, b));

        // when
        MockMultipartFile cFile = new MockMultipartFile("c", "c.png", MediaType.IMAGE_PNG_VALUE, "cFile".getBytes());
        BoardUpdateRequest boardUpdateRequest = createBoardUpdateRequest("update title", "update content",
                List.of(cFile), List.of(a.getId()));
        Board.ImageUpdatedResult imageUpdatedResult = board.update(boardUpdateRequest);

        // then
        Assertions.assertThat(board.getTitle()).isEqualTo(boardUpdateRequest.getTitle());
        Assertions.assertThat(board.getContent()).isEqualTo(boardUpdateRequest.getContent());

        List<Image> resultImages = board.getImages();
        List<String> resultOriginNames = resultImages.stream().map(i -> i.getOriginName()).collect(toList());
        Assertions.assertThat(resultImages.size()).isEqualTo(2);
        Assertions.assertThat(resultOriginNames).contains(b.getOriginName(), cFile.getOriginalFilename());

        List<MultipartFile> addedImageFiles = imageUpdatedResult.getAddedImageFiles();
        Assertions.assertThat(addedImageFiles.size()).isEqualTo(1);
        assertThat(addedImageFiles.get(0).getOriginalFilename()).isEqualTo(cFile.getOriginalFilename());

        List<Image> addedImages = imageUpdatedResult.getAddedImages();
        List<String> addedOriginNames = addedImages.stream().map(i -> i.getOriginName()).collect(toList());
        Assertions.assertThat(addedImages.size()).isEqualTo(1);
        Assertions.assertThat(addedOriginNames).contains(cFile.getOriginalFilename());

        List<Image> deletedImages = imageUpdatedResult.getDeletedImages();
        List<String> deletedOriginNames = deletedImages.stream().map(i -> i.getOriginName()).collect(toList());
        Assertions.assertThat(deletedImages.size()).isEqualTo(1);
        Assertions.assertThat(deletedOriginNames).contains(a.getOriginName());
    }
}
