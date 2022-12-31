package yoon.community.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import yoon.community.dto.user.UserEditRequestDto;
import yoon.community.entity.user.User;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.repository.user.UserRepository;
import yoon.community.response.Response;
import yoon.community.service.user.UserService;

@Api(value = "User Controller", tags = "User")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @ApiOperation(value = "전체 회원 조회", notes = "전체 회원을 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public Response findAllUsers() {
        return Response.success(userService.findAllUsers());
    }

    @ApiOperation(value = "개별 회원 조회", notes = "개별 회원을 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}")
    public Response findUser(@ApiParam(value = "User ID", required = true) @PathVariable Long id) {
        return Response.success(userService.findUser(id));
    }

    @ApiOperation(value = "회원 정보 수정", notes = "회원의 정보를 수정")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users")
    public Response editUserInfo(@RequestBody UserEditRequestDto userEditRequestDto) {
        User user = getPrincipal();
        return Response.success(userService.editUserInfo(user, userEditRequestDto));
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원을 탈퇴 시킴")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/users")
    public Response deleteUserInfo() {
        User user = getPrincipal();
        userService.deleteUserInfo(user);
        return Response.success();
    }

    @ApiOperation(value = "즐겨찾기 한 글 조회", notes = "유저가 즐겨찾기 한 게시글들 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/favorites")
    public Response findFavorites() {
        User user = getPrincipal();
        return Response.success(userService.findFavorites(user));
    }

    public User getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        return user;
    }
}
