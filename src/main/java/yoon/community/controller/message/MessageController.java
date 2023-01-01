package yoon.community.controller.message;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import yoon.community.dto.message.MessageCreateRequest;
import yoon.community.entity.member.Member;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.repository.member.MemberRepository;
import yoon.community.response.Response;
import yoon.community.service.message.MessageService;

import javax.validation.Valid;

@Api(value = "Messages Controller", tags = "Messages")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "편지 작성", notes = "편지 보내기")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/messages")
    public Response createMessage(@Valid @RequestBody MessageCreateRequest req) {
        Member sender = getPrincipal();
        return Response.success(messageService.createMessage(sender, req));
    }

    @ApiOperation(value = "받은 쪽지 전부 확인", notes = "받은 쪽지함 확인")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/messages/receiver")
    public Response receiveMessages() {
        Member member = getPrincipal();
        return Response.success(messageService.receiveMessages(member));
    }

    @ApiOperation(value = "받은 쪽지 중 한 개 확인", notes = "받은 편지 중 하나를 확인")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/messages/receiver/{id}")
    public Response receiveMessage(@ApiParam(value = "쪽지 id", required = true) @PathVariable Long id) {
        Member member = getPrincipal();
        return Response.success(messageService.receiveMessage(id, member));
    }

    @ApiOperation(value = "보낸 쪽지 전부 확인", notes = "보낸 쪽지함 확인")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/messages/sender")
    public Response sendMessages() {
        Member member = getPrincipal();
        return Response.success(messageService.sendMessages(member));
    }

    @ApiOperation(value = "보낸 쪽지 중 한 개 확인", notes = "보낸 쪽지 중 하나를 확인")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/messages/sender/{id}")
    public Response sendMessage(@ApiParam(value = "쪽지 id", required = true) @PathVariable Long id) {
        Member member = getPrincipal();
        return Response.success(messageService.sendMessage(id, member));
    }

    @ApiOperation(value = "받은 쪽지 삭제", notes = "받은 쪽지 삭제하기")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/messages/receiver/{id}")
    public Response deleteReceiveMessage(@ApiParam(value = "쪽지 id", required = true) @PathVariable Long id) {
        Member member = getPrincipal();
        messageService.deleteMessageByReceiver(id, member);
        return Response.success();
    }

    @ApiOperation(value = "보낸 쪽지 삭제", notes = "보낸 쪽지 삭제하기")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/messages/sender/{id}")
    public Response deleteSendMessage(@ApiParam(value = "쪽지 id", required = true) @PathVariable Long id) {
        Member member = getPrincipal();
        messageService.deleteMessageBySender(id, member);
        return Response.success();
    }

    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        return member;
    }
}
