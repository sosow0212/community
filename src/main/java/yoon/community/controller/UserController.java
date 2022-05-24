package yoon.community.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import yoon.community.dto.RegisterDto;
import yoon.community.response.Response;
import yoon.community.service.UserService;

import javax.validation.Valid;

import static yoon.community.response.Response.success;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원가입", notes="회원가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/auth")
    public Response register(@Valid @RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
        return success();
    }

//    @PostMapping("/api/sign-in")
//    @ResponseStatus(HttpStatus.OK)
//    public Response signIn(@Valid RegisterDto registerDto) { // 3
//        return success(userService.signIn(req));
//    }
}
