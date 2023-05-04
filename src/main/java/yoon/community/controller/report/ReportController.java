package yoon.community.controller.report;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import yoon.community.config.guard.JwtAuth;
import yoon.community.domain.member.Member;
import yoon.community.dto.report.BoardReportRequest;
import yoon.community.dto.report.MemberReportRequestDto;
import yoon.community.response.Response;
import yoon.community.service.report.ReportService;

import javax.validation.Valid;

@Api(value = "Report Controller", tags = "Report")
@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    public ReportController(final ReportService reportService) {
        this.reportService = reportService;
    }

    @ApiOperation(value = "유저 신고", notes = "유저를 신고합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reports/users")
    public Response reportUser(@Valid @RequestBody final MemberReportRequestDto req, @JwtAuth final Member member) {
        return Response.success(reportService.reportUser(member, req));
    }

    @ApiOperation(value = "게시글 신고", notes = "게시글을 신고합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reports/boards")
    public Response reportBoard(@Valid @RequestBody final BoardReportRequest req, @JwtAuth final Member member) {
        return Response.success(reportService.reportBoard(member, req));
    }
}
