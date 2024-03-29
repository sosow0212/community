package yoon.community.service.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yoon.community.domain.board.Board;
import yoon.community.domain.board.Favorite;
import yoon.community.domain.board.Image;
import yoon.community.domain.board.LikeBoard;
import yoon.community.domain.category.Category;
import yoon.community.domain.member.Member;
import yoon.community.dto.board.*;
import yoon.community.exception.*;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.board.FavoriteRepository;
import yoon.community.repository.board.LikeBoardRepository;
import yoon.community.repository.category.CategoryRepository;
import yoon.community.service.file.FileService;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
public class BoardService {

    private static final String SUCCESS_LIKE_BOARD = "좋아요 처리 완료";
    private static final String SUCCESS_UNLIKE_BOARD = "좋아요 취소 완료";
    private static final String SUCCESS_FAVORITE_BOARD = "즐겨찾기 처리 완료";
    private static final String SUCCESS_UNFAVORITE_BOARD = "즐겨찾기 취소 완료";
    private static final int RECOMMEND_SET_COUNT = 10;
    private static final int PAGE_SIZE = 10;
    private static final String SORTED_BY_ID = "id";

    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final LikeBoardRepository likeBoardRepository;
    private final FavoriteRepository favoriteRepository;
    private final CategoryRepository categoryRepository;

    public BoardService(final BoardRepository boardRepository, final FileService fileService, final LikeBoardRepository likeBoardRepository, final FavoriteRepository favoriteRepository, final CategoryRepository categoryRepository) {
        this.boardRepository = boardRepository;
        this.fileService = fileService;
        this.likeBoardRepository = likeBoardRepository;
        this.favoriteRepository = favoriteRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public BoardCreateResponse createBoard(final BoardCreateRequest req, final int categoryId, final Member member) {
        List<Image> images = req.getImages().stream()
                .map(image -> Image.from(image.getOriginalFilename()))
                .collect(toList());

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        Board board = boardRepository.save(new Board(req.getTitle(), req.getContent(), member, category, images));

        uploadImages(board.getImages(), req.getImages());
        return BoardCreateResponse.toDto(board);
    }

    @Transactional(readOnly = true)
    public BoardFindAllWithPagingResponseDto findAllBoards(final Integer page, final int categoryId) {
        Page<Board> boards = makePageBoards(page, categoryId);
        return responsePagingBoards(boards);
    }

    private BoardFindAllWithPagingResponseDto responsePagingBoards(final Page<Board> boards) {
        List<BoardSimpleDto> boardSimpleDtoList = boards.stream()
                .map(BoardSimpleDto::toDto)
                .collect(toList());

        return BoardFindAllWithPagingResponseDto.toDto(boardSimpleDtoList, new PageInfoDto(boards));
    }

    private Page<Board> makePageBoards(final Integer page, final int categoryId) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE, Sort.by(SORTED_BY_ID).descending());
        return boardRepository.findAllByCategoryId(pageRequest, categoryId);
    }

    @Transactional(readOnly = true)
    public BoardResponseDto findBoard(final Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFoundException::new);

        Member member = board.getMember();
        return BoardResponseDto.toDto(board, member.getNickname());
    }

    @Transactional
    public String updateLikeOfBoard(final Long id, final Member member) {
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFoundException::new);

        if (!hasLikeBoard(board, member)) {
            board.increaseLikeCount();
            return createLikeBoard(board, member);
        }

        board.decreaseLikeCount();
        return removeLikeBoard(board, member);
    }

    private String removeLikeBoard(final Board board, final Member member) {
        LikeBoard likeBoard = likeBoardRepository.findByBoardAndMember(board, member)
                .orElseThrow(LikeHistoryNotfoundException::new);

        likeBoardRepository.delete(likeBoard);

        return SUCCESS_UNLIKE_BOARD;
    }

    @Transactional
    public String updateOfFavoriteBoard(final Long id, final Member member) {
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFoundException::new);

        if (!hasFavoriteBoard(board, member)) {
            board.increaseFavoriteCount();
            return createFavoriteBoard(board, member);
        }

        board.decreaseFavoriteCount();
        return removeFavoriteBoard(board, member);
    }

    @Transactional(readOnly = true)
    public List<BoardSimpleDto> findBestBoards(final Pageable pageable) {
        Page<Board> boards = boardRepository.findByLikedGreaterThanEqual(pageable, RECOMMEND_SET_COUNT);

        return boards.stream()
                .map(BoardSimpleDto::toDto)
                .collect(toList());
    }

    @Transactional
    public BoardResponseDto editBoard(final Long id, final BoardUpdateRequest req, final Member member) {
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFoundException::new);

        validateBoardOwner(member, board);

        Board.ImageUpdatedResult result = board.update(req);

        uploadImages(result.getAddedImages(), result.getAddedImageFiles());
        deleteImages(result.getDeletedImages());

        return BoardResponseDto.toDto(board, member.getNickname());
    }

    @Transactional
    public void deleteBoard(final Long boardId, final Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        validateBoardOwner(member, board);
        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public List<BoardSimpleDto> searchBoard(final String keyword, final Pageable pageable) {
        Page<Board> boards = boardRepository.findByTitleContaining(keyword, pageable);

        return boards.stream()
                .map(BoardSimpleDto::toDto)
                .collect(toList());
    }

    private void uploadImages(final List<Image> uploadedImages, final List<MultipartFile> fileImages) {
        IntStream.range(0, uploadedImages.size())
                .forEach(uploadedImage -> fileService.upload(
                        fileImages.get(uploadedImage),
                        uploadedImages.get(uploadedImage).getUniqueName())
                );
    }

    private void deleteImages(final List<Image> deletedImages) {
        deletedImages.forEach(deletedImage -> fileService.delete(deletedImage.getUniqueName()));
    }

    public void validateBoardOwner(final Member member, final Board board) {
        if (!member.equals(board.getMember())) {
            throw new MemberNotEqualsException();
        }
    }

    public String createLikeBoard(final Board board, final Member member) {
        LikeBoard likeBoard = new LikeBoard(board, member);
        likeBoardRepository.save(likeBoard);
        return SUCCESS_LIKE_BOARD;
    }

    public boolean hasLikeBoard(final Board board, final Member member) {
        return likeBoardRepository.findByBoardAndMember(board, member)
                .isPresent();
    }

    public String createFavoriteBoard(final Board board, final Member member) {
        Favorite favorite = new Favorite(board, member);
        favoriteRepository.save(favorite);
        return SUCCESS_FAVORITE_BOARD;
    }

    public String removeFavoriteBoard(final Board board, final Member member) {
        Favorite favorite = favoriteRepository.findByBoardAndMember(board, member)
                .orElseThrow(FavoriteNotFoundException::new);

        favoriteRepository.delete(favorite);

        return SUCCESS_UNFAVORITE_BOARD;
    }

    public boolean hasFavoriteBoard(final Board board, final Member member) {
        return favoriteRepository.findByBoardAndMember(board, member)
                .isPresent();
    }
}

