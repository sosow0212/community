package yoon.community.service.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import yoon.community.exception.MemberNotEqualsException;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.board.FavoriteRepository;
import yoon.community.repository.board.LikeBoardRepository;
import yoon.community.repository.category.CategoryRepository;
import yoon.community.repository.user.UserRepository;
import yoon.community.service.file.FileService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final LikeBoardRepository likeBoardRepository;
    private final FavoriteRepository favoriteRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public BoardCreateResponse create(BoardCreateRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        List<Image> images = req.getImages().stream().map(i -> new Image(i.getOriginalFilename())).collect(toList());
        Category category = categoryRepository.findById(req.getCategoryId()).orElseThrow(CategoryNotFoundException::new);

        Board board = boardRepository.save(new Board(req.getTitle(), req.getContent(), user, category, images));

        uploadImages(board.getImages(), req.getImages());
        return new BoardCreateResponse(board.getId(), board.getTitle(), board.getContent());
    }

    @Transactional(readOnly = true)
    public List<BoardSimpleDto> findAllBoards(Pageable pageable, int categoryId) {
        Page<Board> boards = boardRepository.findAllByCategoryId(pageable, categoryId);
        List<BoardSimpleDto> boardSimpleDtoList = new ArrayList<>();
        boards.stream().forEach(i -> boardSimpleDtoList.add(new BoardSimpleDto().toDto(i)));
        return boardSimpleDtoList;
    }

    @Transactional(readOnly = true)
    public BoardDto findBoard(int id) {
        return BoardDto.toDto(boardRepository.findById(id).orElseThrow(BoardNotFoundException::new));
    }

    @Transactional
    public String likeBoard(int id) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        if (likeBoardRepository.findByBoardAndUser(board, user) == null) {
            // 좋아요를 누른적 없다면 LikeBoard 생성 후, 좋아요 처리
            board.setLiked(board.getLiked() + 1);
            LikeBoard likeBoard = new LikeBoard(board, user); // true 처리
            likeBoardRepository.save(likeBoard);
            return "좋아요 처리 완료";
        } else {
            // 좋아요를 누른적 있다면 취소 처리 후 테이블 삭제
            LikeBoard likeBoard = likeBoardRepository.findByBoardAndUser(board, user);
            likeBoard.unLikeBoard(board);
            likeBoardRepository.delete(likeBoard);
            return "좋아요 취소";
        }
    }


    @Transactional
    public String favoriteBoard(int id) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        if (favoriteRepository.findByBoardAndUser(board, user) == null) {
            // 좋아요를 누른적 없다면 Favorite 생성 후, 즐겨찾기 처리
            board.setFavorited(board.getFavorited() + 1);
            Favorite favorite = new Favorite(board, user); // true 처리
            favoriteRepository.save(favorite);
            return "즐겨찾기 처리 완료";
        } else {
            // 즐겨찾기 누른적 있다면 즐겨찾기 처리 후 테이블 삭제
            Favorite favorite = favoriteRepository.findFavoriteByBoard(board);
            favorite.unFavoriteBoard(board);
            favoriteRepository.delete(favorite);
            return "즐겨찾기 취소";
        }
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
    public BoardDto editBoard(int id, BoardUpdateRequest req) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        if (user != board.getUser()) {
            throw new MemberNotEqualsException();
        }

        Board.ImageUpdatedResult result = board.update(req);
        uploadImages(result.getAddedImages(), result.getAddedImageFiles());
        deleteImages(result.getDeletedImages());
        return BoardDto.toDto(board);
    }

    @Transactional
    public void deleteBoard(int id) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        if (user != board.getUser()) {
            throw new MemberNotEqualsException();
        }

        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public List<BoardSimpleDto> search(String keyword, Pageable pageable) {
        Page<Board> boards = boardRepository.findByTitleContaining(keyword, pageable);
        List<BoardSimpleDto> boardSimpleDtoList = new ArrayList<>();
        boards.stream().forEach(i -> boardSimpleDtoList.add(new BoardSimpleDto().toDto(i)));
        return boardSimpleDtoList;
    }


    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size()).forEach(i -> fileService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }

    private void deleteImages(List<Image> images) {
        images.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }
}

