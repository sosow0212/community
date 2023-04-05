package yoon.community.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static yoon.community.factory.UserFactory.createUser;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import yoon.community.config.jwt.TokenProvider;
import yoon.community.dto.sign.LoginRequestDto;
import yoon.community.dto.sign.SignUpRequestDto;
import yoon.community.exception.LoginFailureException;
import yoon.community.repository.member.MemberRepository;
import yoon.community.repository.point.PointRepository;
import yoon.community.repository.refreshToken.RefreshTokenRepository;
import yoon.community.service.auth.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PointRepository pointRepository;

    @Mock
    RedisTemplate<String, String> redisTemplate;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("회원가입을 한다.")
    void sign_up_success() {
        // given
        SignUpRequestDto req = new SignUpRequestDto("username", "1234", "name", "nickname");
        given(memberRepository.existsByNickname(req.getNickname())).willReturn(false);

        // when
        authService.signup(req);

        // then
        verify(passwordEncoder).encode(req.getPassword());
        verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("로그인에 실패한다.")
    void throws_exception_when_login_with_username_failure() {
        // given
        given(memberRepository.findByUsername(any())).willReturn(Optional.of(createUser()));

        // when, then
        assertThatThrownBy(() -> authService.signIn(new LoginRequestDto("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @Test
    @DisplayName("패스워드 검증을 한다.")
    void throws_exception_when_login_with_password_invalid() {
        // given
        given(memberRepository.findByUsername(any())).willReturn(Optional.of(createUser()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> authService.signIn(new LoginRequestDto("username", "password")))
                .isInstanceOf(LoginFailureException.class);
    }
}
