package yoon.community.controller.board;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import yoon.community.config.guard.JwtAuth;
import yoon.community.domain.member.Member;
import yoon.community.dto.board.BoardCreateRequest;
import yoon.community.dto.board.BoardUpdateRequest;
import yoon.community.response.Response;
import yoon.community.service.board.BoardService;

import javax.validation.Valid;

@Api(value = "Board Controller", tags = "Board")
@RestController
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
    }

    @ApiOperation(value = "게시글 생성", notes = "게시글을 작성합니다.")
    @PostMapping("/boards")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createBoard(@Valid @ModelAttribute final BoardCreateRequest req,
                                @RequestParam(value = "category", defaultValue = "1") final int categoryId,
                                @JwtAuth final Member member) {
        // http://localhost:8080/api/boards?category=3
        return Response.success(boardService.createBoard(req, categoryId, member));
    }

    @ApiOperation(value = "게시글 목록 조회", notes = "게시글 목록을 조회합니다.")
    @GetMapping("/boards/all/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllBoards(@ApiParam(value = "카테고리 id", required = true) @PathVariable final int categoryId,
                                  @RequestParam(defaultValue = "0") final Integer page) {
        // ex) http://localhost:8080/api/boards/all/{categoryId}?page=0
        return Response.success(boardService.findAllBoards(page, categoryId));
    }

    @ApiOperation(value = "게시글 단건 조회", notes = "게시글을 단건 조회합니다.")
    @GetMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findBoard(@ApiParam(value = "게시글 id", required = true) @PathVariable final Long id) {
        return Response.success(boardService.findBoard(id));
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정합니다.")
    @PutMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response editBoard(@ApiParam(value = "게시글 id", required = true) @PathVariable final Long id,
                              @Valid @ModelAttribute final BoardUpdateRequest req,
                              @JwtAuth final Member member) {
        return Response.success(boardService.editBoard(id, req, member));
    }

    @ApiOperation(value = "게시글 좋아요", notes = "사용자가 게시글 좋아요를 누릅니다.")
    @PostMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response likeBoard(@ApiParam(value = "게시글 id", required = true) @PathVariable final Long id,
                              @JwtAuth final Member member) {
        return Response.success(boardService.updateLikeOfBoard(id, member));
    }

    @ApiOperation(value = "게시글 즐겨찾기", notes = "사용자가 게시글 즐겨찾기를 누릅니다.")
    @PostMapping("/boards/{id}/favorites")
    @ResponseStatus(HttpStatus.OK)
    public Response favoriteBoard(@ApiParam(value = "게시글 id", required = true) @PathVariable final Long id,
                                  @JwtAuth Member member) {
        return Response.success(boardService.updateOfFavoriteBoard(id, member));
    }

    @ApiOperation(value = "인기글 조회", notes = "추천수 10이상 게시글을 조회합니다.")
    @GetMapping("/boards/best")
    @ResponseStatus(HttpStatus.OK)
    public Response getBestBoards(
            @PageableDefault(size = 5, sort = "liked", direction = Sort.Direction.DESC) final Pageable pageable) {
        return Response.success(boardService.findBestBoards(pageable));
    }

    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제합니다.")
    @DeleteMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteBoard(@ApiParam(value = "게시글 id", required = true) @PathVariable final Long id,
                                @JwtAuth final Member member) {
        boardService.deleteBoard(id, member);
        return Response.success();
    }

    @ApiOperation(value = "게시글 검색", notes = "게시글을 검색합니다.")
    @GetMapping("/boards/search")
    @ResponseStatus(HttpStatus.OK)
    public Response searchBoard(final String keyword,
                                @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) final Pageable pageable) {
        // ex) http://localhost:8080/api/boards/search?page=0
        return Response.success(boardService.searchBoard(keyword, pageable));
    }
}
