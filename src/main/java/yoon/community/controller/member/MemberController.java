package yoon.community.controller.member;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import yoon.community.dto.member.MemberEditRequestDto;
import yoon.community.entity.member.Member;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.repository.member.MemberRepository;
import yoon.community.response.Response;
import yoon.community.service.member.MemberService;

@Api(value = "User Controller", tags = "User")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "전체 회원 조회", notes = "전체 회원을 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public Response findAllMembers() {
        return Response.success(memberService.findAllMembers());
    }

    @ApiOperation(value = "개별 회원 조회", notes = "개별 회원을 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}")
    public Response findMember(@ApiParam(value = "User ID", required = true) @PathVariable Long id) {
        return Response.success(memberService.findMember(id));
    }

    @ApiOperation(value = "회원 정보 수정", notes = "회원의 정보를 수정")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users")
    public Response editMemberInfo(@RequestBody MemberEditRequestDto memberEditRequestDto) {
        Member member = getPrincipal();
        return Response.success(memberService.editMemberInfo(member, memberEditRequestDto));
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원을 탈퇴 시킴")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/users")
    public Response deleteMemberInfo() {
        Member member = getPrincipal();
        memberService.deleteMemberInfo(member);
        return Response.success();
    }

    @ApiOperation(value = "즐겨찾기 한 글 조회", notes = "유저가 즐겨찾기 한 게시글들 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/favorites")
    public Response findFavorites() {
        Member member = getPrincipal();
        return Response.success(memberService.findFavorites(member));
    }

    public Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName())
                .orElseThrow(MemberNotFoundException::new);
        return member;
    }
}
