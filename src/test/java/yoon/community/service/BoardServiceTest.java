package yoon.community.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import yoon.community.dto.board.BoardCreateRequest;
import yoon.community.dto.board.BoardCreateResponse;
import yoon.community.dto.board.BoardDto;
import yoon.community.dto.board.BoardUpdateRequest;
import yoon.community.entity.board.Board;
import yoon.community.entity.board.Image;
import yoon.community.entity.user.User;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.board.FavoriteRepository;
import yoon.community.repository.board.LikeBoardRepository;
import yoon.community.repository.category.CategoryRepository;
import yoon.community.repository.user.UserRepository;
import yoon.community.service.board.BoardService;
import yoon.community.service.file.FileService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static yoon.community.factory.BoardFactory.createBoard;
import static yoon.community.factory.BoardFactory.createBoardWithImages;
import static yoon.community.factory.CategoryFactory.createCategory;
import static yoon.community.factory.ImageFactory.createImage;
import static yoon.community.factory.ImageFactory.createImageWithIdAndOriginName;
import static yoon.community.factory.UserFactory.createUser;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    @InjectMocks
    BoardService boardService;

    @Mock
    UserRepository userRepository;
    @Mock
    BoardRepository boardRepository;
    @Mock
    FileService fileService;
    @Mock
    LikeBoardRepository likeBoardRepository;
    @Mock
    FavoriteRepository favoriteRepository;
    @Mock
    CategoryRepository categoryRepository;


    @Test
    @DisplayName("create 서비스 테스트")
    void createTest() {
        // given
        BoardCreateRequest req = new BoardCreateRequest("title", "content", List.of(
                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes()),
                new MockMultipartFile("test3", "test3.PNG", MediaType.IMAGE_PNG_VALUE, "test3".getBytes())
        ));

        int categoryId = anyInt();
        User user = createUser();

        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(createCategory()));
        given(boardRepository.save(any())).willReturn(createBoardWithImages(
                IntStream.range(0, req.getImages().size()).mapToObj(i -> createImage()).collect(toList()))
        );

        // when
        BoardCreateResponse result = boardService.create(req, categoryId, user);

        // then
        assertThat(result.getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("findAllBoards 서비스 테스트")
    void findAllBoardsTest() {
//        // given
//        Pageable pageable =  PageRequest.of(3, 5, Sort.by("createdAt").descending());
//        int categoryId = 0;
//        List<Image> images = new ArrayList<>();
//        images.add(createImage());
//        Board board = createBoardWithImages(images);
//        Page<Board> boards = null;
//        given(boardRepository.findAllByCategoryId(any(), categoryId)).willReturn(boards);
//
//        // when
//        List<BoardSimpleDto> result = boardService.findAllBoards(any(), categoryId);
//
//        // then
//        verify(boardService.findAllBoards(pageable, anyInt()));
    }


    @Test
    @DisplayName("findBoard 서비스 테스트")
    void findBoardTest() {
        // given
        int id = 1;
        Board board = createBoard();

        given(boardRepository.findById(anyInt())).willReturn(Optional.of(board));

        // when
        BoardDto result = boardService.findBoard(id);

        // then
        assertThat(result.getTitle()).isEqualTo("title");
    }


    @Test
    @DisplayName("likeBoard 서비스 테스트")
    void likeBoardTest() {
        // given
        int id = 1;
        User user = createUser();
        Board board = createBoard();
        given(boardRepository.findById(any())).willReturn(Optional.of(board));

        // when
        String result = boardService.likeBoard(id, user);

        // then
        assertThat(result).isEqualTo("좋아요 처리 완료");
    }

    @Test
    @DisplayName("favoriteBoard 서비스 테스트")
    void favoriteBoardTest() {
        // given
        int id = 1;
        User user = createUser();
        Board board = createBoard();
        given(boardRepository.findById(any())).willReturn(Optional.of(board));

        // when
        String result = boardService.favoriteBoard(id, user);

        // then
        assertThat(result).isEqualTo("즐겨찾기 처리 완료");
    }

    @Test
    @DisplayName("editBoard 서비스 테스트")
    void editBoardTest() {
//        // given
//        Image a = createImageWithIdAndOriginName(1L, "a.png");
//        Image b = createImageWithIdAndOriginName(2L, "b.png");
//        Board board = createBoardWithImages(List.of(a, b));
//        given(boardRepository.findById(anyInt())).willReturn(Optional.of(board));
//        MockMultipartFile cFile = new MockMultipartFile("c", "c.png", MediaType.IMAGE_PNG_VALUE, "c".getBytes());
//        BoardUpdateRequest req = new BoardUpdateRequest("title2", "content2", List.of(cFile), List.of(a.getId()));
//
//        // when
//        BoardDto result = boardService.editBoard(1, req, createUser());
//
//        // then
//        assertThat(result.getTitle()).isEqualTo("title2");
    }


    @Test
    @DisplayName("deleteBoard 서비스 테스트")
    void deleteBoardTest() {
        // given
        User user = createUser();
        given(boardRepository.findById(anyInt())).willReturn(Optional.of(createBoard()));

        // when
        boardService.deleteBoard(anyInt(), createUser());

        // then
        verify(boardRepository).delete(any());
    }
}
