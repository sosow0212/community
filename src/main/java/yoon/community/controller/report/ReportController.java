package yoon.community.controller.report;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import yoon.community.dto.report.BoardReportRequest;
import yoon.community.dto.report.UserReportRequest;
import yoon.community.entity.user.User;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.repository.user.UserRepository;
import yoon.community.response.Response;
import yoon.community.service.report.ReportService;

import javax.validation.Valid;

@Api(value = "Report Controller", tags = "Report")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepository;

    @ApiOperation(value = "유저 신고", notes = "유저를 신고합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reports/users")
    public Response reportUser(@Valid @RequestBody UserReportRequest userReportRequest) {
        User user = getPrincipal();
        return Response.success(reportService.reportUser(user, userReportRequest));
    }

    @ApiOperation(value = "게시글 신고", notes = "게시글을 신고합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reports/boards")
    public Response reportBoard(@Valid @RequestBody BoardReportRequest boardReportRequest) {
        User user = getPrincipal();
        return Response.success(reportService.reportBoard(user, boardReportRequest));
    }


//    @ApiOperation(value = "게시글 신고 해제", notes = "게시글을 신고 해제합니다.")
//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping("/reports/boards/{id}")
//    public Response unReportBoard(@ApiParam(value = "게시글 id", required = true) @PathVariable int id) {
//        return Response.success();
//    }
//
//    @ApiOperation(value = "유저 신고 해제", notes = "유저를 신고 해제합니다.")
//    @ResponseStatus(HttpStatus.OK)
//    @PostMapping("/reports/users/{id}")
//    public Response unReportUser(@ApiParam(value = "유저 id", required = true) @PathVariable int id) {
//        return Response.success();
//    }

    private User getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        return user;
    }
}
