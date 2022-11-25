package yoon.community.service.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yoon.community.dto.board.*;
import yoon.community.entity.board.Board;
import yoon.community.entity.board.Favorite;
import yoon.community.entity.board.Image;
import yoon.community.entity.board.LikeBoard;
import yoon.community.entity.category.Category;
import yoon.community.entity.user.User;
import yoon.community.exception.BoardNotFoundException;
import yoon.community.exception.CategoryNotFoundException;
import yoon.community.exception.FavoriteNotFoundException;
import yoon.community.exception.MemberNotEqualsException;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.board.FavoriteRepository;
import yoon.community.repository.board.LikeBoardRepository;
import yoon.community.repository.category.CategoryRepository;
import yoon.community.service.file.FileService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final static String SUCCESS_LIKE_BOARD = "좋아요 처리 완료";
    private final static String SUCCESS_UNLIKE_BOARD = "좋아요 취소 완료";
    private final static String SUCCESS_FAVORITE_BOARD = "즐겨찾기 처리 완료";
    private final static String SUCCESS_UNFAVORITE_BOARD = "즐겨찾기 취소 완료";

    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final LikeBoardRepository likeBoardRepository;
    private final FavoriteRepository favoriteRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public BoardCreateResponse createBoard(BoardCreateRequest req, int categoryId, User user) {
        List<Image> images = req.getImages().stream().map(i -> new Image(i.getOriginalFilename())).collect(toList());
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        Board board = boardRepository.save(new Board(req.getTitle(), req.getContent(), user, category, images));

        uploadImages(board.getImages(), req.getImages());
        return new BoardCreateResponse(board.getId(), board.getTitle(), board.getContent());
    }

    @Transactional(readOnly = true)
    public List<BoardSimpleDto> findAllBoards(Pageable pageable, int categoryId) {
        Page<Board> boards = boardRepository.findAllByCategoryId(pageable, categoryId);
        List<BoardSimpleDto> boardSimpleDtoList = new ArrayList<>();
        boards.stream().map(i -> boardSimpleDtoList.add(new BoardSimpleDto().toDto(i)));
        return boardSimpleDtoList;
    }

    @Transactional(readOnly = true)
    public BoardDto findBoard(int id) {
        return BoardDto.toDto(boardRepository.findById(id).orElseThrow(BoardNotFoundException::new));
    }

    @Transactional
    public String updateLikeOfBoard(int id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        if (!hasLikeBoard(board, user)) {
            board.increaseLikeCount();
            return createLikeBoard(board, user);
        }
        board.decreaseLikeCount();
        return removeLikeBoard(board, user);
    }

    @Transactional
    public String updateOfFavoriteBoard(int id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        if (!hasFavoriteBoard(board, user)) {
            board.increaseFavoriteCount();
            return createFavoriteBoard(board, user);
        }
        board.decreaseFavoriteCount();
        return removeFavoriteBoard(board, user);
    }

    @Transactional(readOnly = true)
    public List<BoardSimpleDto> findBestBoards(Pageable pageable) {
        // 10 이상은 추천글
        Page<Board> boards = boardRepository.findByLikedGreaterThanEqual(pageable, 10);
        List<BoardSimpleDto> boardSimpleDtoList = new ArrayList<>();
        boards.stream().forEach(i -> boardSimpleDtoList.add(new BoardSimpleDto().toDto(i)));
        return boardSimpleDtoList;
    }

    @Transactional
    public BoardDto editBoard(int id, BoardUpdateRequest req, User user) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        validateBoardOwner(user, board);
        Board.ImageUpdatedResult result = board.update(req);
        uploadImages(result.getAddedImages(), result.getAddedImageFiles());
        deleteImages(result.getDeletedImages());
        return BoardDto.toDto(board);
    }

    @Transactional
    public void deleteBoard(int id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        validateBoardOwner(user, board);
        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public List<BoardSimpleDto> searchBoard(String keyword, Pageable pageable) {
        Page<Board> boards = boardRepository.findByTitleContaining(keyword, pageable);
        List<BoardSimpleDto> boardSimpleDtoList = new ArrayList<>();
        boards.stream().map(i -> boardSimpleDtoList.add(new BoardSimpleDto().toDto(i)));
        return boardSimpleDtoList;
    }

    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size())
                .forEach(i -> fileService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }

    private void deleteImages(List<Image> images) {
        images.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }


    public void validateBoardOwner(User user, Board board) {
        if (!user.equals(board.getUser())) {
            throw new MemberNotEqualsException();
        }
    }

    public String createLikeBoard(Board board, User user) {
        LikeBoard likeBoard = new LikeBoard(board, user); // true 처리
        likeBoardRepository.save(likeBoard);
        return SUCCESS_LIKE_BOARD;
    }

    public String removeLikeBoard(Board board, User user) {
        LikeBoard likeBoard = likeBoardRepository.findByBoardAndUser(board, user).get();
        likeBoardRepository.delete(likeBoard);
        return SUCCESS_UNLIKE_BOARD;
    }

    public boolean hasLikeBoard(Board board, User user) {
        if (likeBoardRepository.findByBoardAndUser(board, user).isEmpty()) {
            return false;
        }
        return true;
    }

    public String createFavoriteBoard(Board board, User user) {
        Favorite favorite = new Favorite(board, user); // true 처리
        favoriteRepository.save(favorite);
        return SUCCESS_FAVORITE_BOARD;
    }

    public String removeFavoriteBoard(Board board, User user) {
        Favorite favorite = favoriteRepository.findByBoardAndUser(board, user)
                .orElseThrow(FavoriteNotFoundException::new);
        favoriteRepository.delete(favorite);
        return SUCCESS_UNFAVORITE_BOARD;
    }

    public boolean hasFavoriteBoard(Board board, User user) {
        if (favoriteRepository.findByBoardAndUser(board, user).isEmpty()) {
            return false;
        }
        return true;
    }
}

