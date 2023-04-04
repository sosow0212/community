package yoon.community.service;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static yoon.community.factory.BoardFactory.createBoard;
import static yoon.community.factory.BoardFactory.createBoardWithImages;
import static yoon.community.factory.CategoryFactory.createCategory;
import static yoon.community.factory.FavoriteFactory.createFavoriteWithFavorite;
import static yoon.community.factory.ImageFactory.createImage;
import static yoon.community.factory.UserFactory.createUser;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import yoon.community.domain.board.Board;
import yoon.community.domain.board.Favorite;
import yoon.community.domain.board.LikeBoard;
import yoon.community.domain.member.Member;
import yoon.community.dto.board.BoardCreateRequest;
import yoon.community.dto.board.BoardCreateResponse;
import yoon.community.dto.board.BoardResponseDto;
import yoon.community.dto.board.BoardUpdateRequest;
import yoon.community.exception.BoardNotFoundException;
import yoon.community.exception.CategoryNotFoundException;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.board.FavoriteRepository;
import yoon.community.repository.board.LikeBoardRepository;
import yoon.community.repository.category.CategoryRepository;
import yoon.community.service.board.BoardService;
import yoon.community.service.file.FileService;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    private final static String PROCESS_LIKE_BOARD = "좋아요 처리 완료";
    private final static String PROCESS_UNLIKE_BOARD = "좋아요 취소 완료";
    private final static String PROCESS_FAVORITE_BOARD = "즐겨찾기 처리 완료";
    private final static String PROCESS_UNFAVORITE_BOARD = "즐겨찾기 취소 완료";

    @InjectMocks
    BoardService boardService;

    @Mock
    BoardRepository boardRepository;
    @Mock
    LikeBoardRepository likeBoardRepository;
    @Mock
    FavoriteRepository favoriteRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    FileService fileService;

    @Test
    @DisplayName("게시글을 생성한다.")
    void create_board_success() {
        // given
        BoardCreateRequest req = new BoardCreateRequest("title", "content", List.of(
                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes()),
                new MockMultipartFile("test3", "test3.PNG", MediaType.IMAGE_PNG_VALUE, "test3".getBytes())
        ));

        int categoryId = anyInt();
        Member member = createUser();

        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(createCategory()));
        given(boardRepository.save(any())).willReturn(createBoardWithImages(
                IntStream.range(0, req.getImages().size()).mapToObj(i -> createImage()).collect(toList()))
        );

        // when
        BoardCreateResponse result = boardService.createBoard(req, categoryId, member);

        // then
        assertThat(result.getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("카테고리를 찾지 못해서 게시글 생성에 실패한다.")
    void create_board_fail_when_category_can_not_found() {
        // given
        BoardCreateRequest req = new BoardCreateRequest("title", "content", List.of(
                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes()),
                new MockMultipartFile("test3", "test3.PNG", MediaType.IMAGE_PNG_VALUE, "test3".getBytes())
        ));

        int categoryId = anyInt();
        Member member = createUser();

        // when & then
        assertThatThrownBy(() -> boardService.createBoard(req, categoryId, member))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("게시글을 모두 찾는다.")
    void find_all_board_success() {
        // given
        String sort = "likeCount";
        Integer page = 0;
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(sort).descending().and(Sort.by("id")));

        assertThat(pageRequest.getPageNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글을 찾는다.")
    void find_board_success() {
        // given
        Long id = 1L;
        Board board = createBoard();

        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        // when
        BoardResponseDto result = boardService.findBoard(id);

        // then
        assertThat(result.getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("올바른 게시글 번호가 아니라서 게시글 찾는데 실패한다.")
    void find_board_fail_when_board_id_invalid() {
        // given
        Long id = 1L;
        Member member = createUser();
        Board board = createBoard(member);

        // when & then
        assertThatThrownBy(() -> boardService.findBoard(id))
                .isInstanceOf(BoardNotFoundException.class);
    }

    @Test
    @DisplayName("게시글 좋아요 처리를한다.")
    void like_board_success() {
        // given
        Member member = createUser();
        Board board = createBoard();

        // when
        String result = boardService.createLikeBoard(board, member);

        // then
        assertThat(result).isEqualTo(PROCESS_LIKE_BOARD);
        verify(likeBoardRepository).save(any());
    }

    @Test
    @DisplayName("게시글 좋아요 취소를 한다.")
    void unlike_board_success() {
        // given
        Long id = 1L;

        Member member = createUser();
        Board board = createBoard();
        board.increaseLikeCount();

        LikeBoard likeBoard = new LikeBoard(id, board, member, true);
        given(boardRepository.findById(id)).willReturn(Optional.of(board));
        given(likeBoardRepository.findByBoardAndMember(board, member)).willReturn(Optional.of(likeBoard));

        // when
        String result = boardService.updateLikeOfBoard(id, member);

        // then
        assertThat(result).isEqualTo(PROCESS_UNLIKE_BOARD);
        verify(likeBoardRepository).delete(likeBoard);
    }

    @Test
    @DisplayName("게시글을 찾지 못해서 좋아요 및 좋아요 취소를 하지 못한다.")
    void like_board_fail_when_board_id_invalid() {
        // given
        Long id = 1L;
        Member member = createUser();
        Board board = createBoard();

        // when & then
        assertThatThrownBy(() -> boardService.updateLikeOfBoard(id, member))
                .isInstanceOf(BoardNotFoundException.class);
    }

    @Test
    @DisplayName("유저가 좋아요를 이미 클릭했는지 확인한다.")
    public void check_user_click_like_button_already_success() {
        // given
        Board board = createBoard();
        Member member = createUser();
        LikeBoard likeBoard = new LikeBoard(1L, board, member, true);
        given(likeBoardRepository.findByBoardAndMember(board, member)).willReturn(Optional.of(likeBoard));

        // when
        boolean result = boardService.hasLikeBoard(board, member);

        // then
        assertThat(result).isEqualTo(true);
    }


    @Test
    @DisplayName("게시글 즐겨찾기 처리를 한다.")
    void favorite_board_success() {
        // given
        Member member = createUser();
        Board board = createBoard();
        board.setFavorited(0);

        // when
        String result = boardService.createFavoriteBoard(board, member);

        // then
        assertThat(result).isEqualTo(PROCESS_FAVORITE_BOARD);
        verify(favoriteRepository).save(any());
    }

    @Test
    @DisplayName("게시글 즐겨찾기를 취소 처리한다.")
    void unfavorite_board_success() {
        // given
        Member member = createUser();
        Board board = createBoard();
        board.setFavorited(1);
        Favorite favorite = createFavoriteWithFavorite(board, member);
        given(favoriteRepository.findByBoardAndMember(board, member)).willReturn(Optional.of(favorite));

        // when
        String result = boardService.removeFavoriteBoard(board, member);

        // then
        assertThat(result).isEqualTo(PROCESS_UNFAVORITE_BOARD);
        verify(favoriteRepository).delete(any());
    }

    @Test
    @DisplayName("게시글을 찾지 못한 경우 즐겨찾기 처리를 하지 못한다.")
    void favorite_fail_when_board_id_invalid() {
        // given
        Long id = 1L;
        Member member = createUser();
        Board board = createBoard();

        // when & then
        assertThatThrownBy(() -> boardService.updateOfFavoriteBoard(id, member))
                .isInstanceOf(BoardNotFoundException.class);
    }

    @Test
    @DisplayName("유저가 즐겨찾기를 이미 클릭했는지 확인한다.")
    public void check_user_click_favorite_button_already_success() {
        // given
        Board board = createBoard();
        Member member = createUser();
        Favorite favorite = createFavoriteWithFavorite(board, member);
        given(favoriteRepository.findByBoardAndMember(board, member)).willReturn(Optional.of(favorite));

        // when
        boolean result = boardService.hasFavoriteBoard(board, member);

        // then
        assertThat(result).isEqualTo(true);
    }


    @Test
    @DisplayName("게시글을 수정한다.")
    void edit_board_success() {
        // given
        Long id = 2L;
        BoardUpdateRequest req = new BoardUpdateRequest();
        req.setTitle("수정");
        req.setContent("수정");
        Member member = createUser();
        Board board = createBoard(member);
        board.setId(id);

        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        // when
        BoardResponseDto result = boardService.editBoard(id, req, member);

        // then
        assertThat(result.getTitle()).isEqualTo(req.getTitle());
    }

    @Test
    @DisplayName("게시글을 찾지 못해서 수정에 실패한다.")
    void edit_board_fail_when_board_id_not_found() {
        // given
        Long id = 1L;
        BoardUpdateRequest req = new BoardUpdateRequest();
        Member member = createUser();

        // when & then
        assertThatThrownBy(() -> boardService.editBoard(id, req, member))
                .isInstanceOf(BoardNotFoundException.class);
    }

    @Test
    @DisplayName("게시글을 제거한다.")
    void delete_board_success() {
        // given
        Member member = createUser();
        Board board = createBoard(member);
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        // when
        boardService.deleteBoard(anyLong(), member);

        // then
        verify(boardRepository).delete(any());
    }

    @Test
    @DisplayName("게시글의 주인이 달라서 게시글 제거에 실패한다.")
    void delete_board_fail_when_owner_invalid() {
        // given
        Member member = createUser();
        Board board = createBoard(member);
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        // when & then
        assertThatThrownBy(() -> boardService.deleteBoard(anyLong(), createUser()));
    }
}
