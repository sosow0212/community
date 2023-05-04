package yoon.community.controller.member;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import yoon.community.config.guard.JwtAuth;
import yoon.community.domain.member.Member;
import yoon.community.dto.member.MemberEditRequestDto;
import yoon.community.response.Response;
import yoon.community.service.member.MemberService;

@Api(value = "User Controller", tags = "User")
@RequestMapping("/api")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @ApiOperation(value = "전체 회원 조회", notes = "전체 회원을 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public Response findAllMembers() {
        return Response.success(memberService.findAllMembers());
    }

    @ApiOperation(value = "개별 회원 조회", notes = "개별 회원을 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}")
    public Response findMember(@ApiParam(value = "User ID", required = true) @PathVariable final Long id) {
        return Response.success(memberService.findMember(id));
    }

    @ApiOperation(value = "회원 정보 수정", notes = "회원의 정보를 수정")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users")
    public Response editMemberInfo(@RequestBody final MemberEditRequestDto memberEditRequestDto, @JwtAuth final Member member) {
        return Response.success(memberService.editMemberInfo(member, memberEditRequestDto));
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원을 탈퇴 시킴")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/users")
    public Response deleteMemberInfo(@JwtAuth final Member member) {
        memberService.deleteMemberInfo(member);
        return Response.success();
    }

    @ApiOperation(value = "즐겨찾기 한 글 조회", notes = "유저가 즐겨찾기 한 게시글들 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/favorites")
    public Response findFavorites(@JwtAuth final Member member) {
        return Response.success(memberService.findFavorites(member));
    }
}
