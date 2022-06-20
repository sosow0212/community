package yoon.community.controller.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import yoon.community.dto.sign.LoginRequestDto;
import yoon.community.dto.sign.SignUpRequestDto;
import yoon.community.dto.sign.TokenRequestDto;
import yoon.community.response.Response;
import yoon.community.service.auth.AuthService;

import javax.validation.Valid;

import static yoon.community.response.Response.success;

@Api(value = "Sign Controller", tags = "Sign")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "회원가입", notes = "회원가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up")
    public Response register(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        return success();
    }

    @ApiOperation(value = "로그인", notes = "로그인을 한다.")
    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@Valid @RequestBody LoginRequestDto req) {
        return success(authService.signIn(req));
    }


    @ApiOperation(value = "토큰 재발급", notes = "토큰 재발급 요청")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reissue")
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return success(authService.reissue(tokenRequestDto));
    }

}
