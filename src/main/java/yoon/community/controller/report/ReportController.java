package yoon.community.controller.report;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import yoon.community.dto.report.BoardReportRequest;
import yoon.community.dto.report.MemberReportRequestDto;
import yoon.community.domain.member.Member;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.repository.member.MemberRepository;
import yoon.community.response.Response;
import yoon.community.service.report.ReportService;

import javax.validation.Valid;

@Api(value = "Report Controller", tags = "Report")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReportController {
    private final ReportService reportService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "유저 신고", notes = "유저를 신고합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reports/users")
    public Response reportUser(@Valid @RequestBody MemberReportRequestDto memberReportRequestDto) {
        return Response.success(reportService.reportUser(getPrincipal(), memberReportRequestDto));
    }

    @ApiOperation(value = "게시글 신고", notes = "게시글을 신고합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reports/boards")
    public Response reportBoard(@Valid @RequestBody BoardReportRequest boardReportRequest) {
        return Response.success(reportService.reportBoard(getPrincipal(), boardReportRequest));
    }


    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        return member;
    }
}
