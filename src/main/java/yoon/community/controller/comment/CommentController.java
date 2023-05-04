package yoon.community.controller.comment;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import yoon.community.config.guard.JwtAuth;
import yoon.community.domain.member.Member;
import yoon.community.dto.comment.CommentCreateRequest;
import yoon.community.dto.comment.CommentReadCondition;
import yoon.community.response.Response;
import yoon.community.service.comment.CommentService;

import javax.validation.Valid;

@Api(value = "Comment Controller", tags = "Comment ")
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(final CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation(value = "댓글 목록 조회", notes = "댓글을 조회 합니다.")
    @GetMapping("/comments")
    @ResponseStatus(HttpStatus.OK)
    public Response findAll(@Valid final CommentReadCondition condition) {
        return Response.success(commentService.findAllComments(condition));
    }

    @ApiOperation(value = "댓글 작성", notes = "댓글을 작성 합니다.")
    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @RequestBody final CommentCreateRequest req, final @JwtAuth Member member) {
        return Response.success(commentService.createComment(req, member));
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제 합니다.")
    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@ApiParam(value = "댓글 id", required = true) @PathVariable final Long id, @JwtAuth Member member) {
        commentService.deleteComment(id, member);
        return Response.success();
    }
}
