package yoon.community.controller.board;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import yoon.community.dto.board.BoardCreateRequest;
import yoon.community.dto.board.BoardReadCondition;
import yoon.community.dto.board.BoardUpdateRequest;
import yoon.community.entity.board.Board;
import yoon.community.response.Response;
import yoon.community.service.board.BoardService;

import javax.validation.Valid;

@Api(value = "Board Controller", tags = "Board")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class BoardController {
    private final BoardService boardService;

    @ApiOperation(value ="게시글 생성", notes = "게시글을 작성합니다.")
    @PostMapping("/boards")
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @ModelAttribute BoardCreateRequest req) {
        return Response.success(boardService.create(req));
    }

    @ApiOperation(value = "게시글 목록 조회", notes = "게시글 목록을 조회합니다.")
    @GetMapping("/boards")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllBoards(@RequestParam(value = "page", defaultValue = "0") int page) {
        return Response.success(boardService.findAllBoards(page));
    }

    @ApiOperation(value = "게시글 단건 조회", notes = "게시글을 단건 조회합니다.")
    @GetMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findBoard(@ApiParam(value = "게시글 id", required = true) @PathVariable int id) {
        return Response.success(boardService.findBoard(id));
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정합니다.")
    @PutMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response editBoard(@PathVariable int id, @Valid @ModelAttribute BoardUpdateRequest req) {
        return Response.success(boardService.editBoard(id, req));
    }

    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제합니다..")
    @DeleteMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteBoard(@PathVariable int id) {
        boardService.deleteBoard(id);
        return Response.success();
    }

}
