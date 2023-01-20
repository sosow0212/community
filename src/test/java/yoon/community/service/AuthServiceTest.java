package yoon.community.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import yoon.community.config.constant.Constant;
import yoon.community.config.jwt.TokenProvider;
import yoon.community.domain.member.Member;
import yoon.community.dto.sign.*;
import yoon.community.exception.LoginFailureException;
import yoon.community.repository.point.PointRepository;
import yoon.community.repository.refreshToken.RefreshTokenRepository;
import yoon.community.repository.member.MemberRepository;
import yoon.community.service.auth.AuthService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static yoon.community.factory.UserFactory.createUser;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

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

    @BeforeEach
    void beforeEach() {
        authService = new AuthService(authenticationManagerBuilder, memberRepository, pointRepository, redisTemplate,
                passwordEncoder, tokenProvider, refreshTokenRepository);
    }

    @Test
    @DisplayName("signup 서비스 테스트")
    void signupTest() {
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
    @DisplayName("로그인 실패 테스트")
    void signInExceptionByNoneMemberTest() {
        // given
        given(memberRepository.findByUsername(any())).willReturn(Optional.of(createUser()));

        // when, then
        assertThatThrownBy(() -> authService.signIn(new LoginRequestDto("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @Test
    @DisplayName("패스워드 검증 테스트")
    void signInExceptionByInvalidPasswordTest() {
        // given
        given(memberRepository.findByUsername(any())).willReturn(Optional.of(createUser()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> authService.signIn(new LoginRequestDto("username", "password")))
                .isInstanceOf(LoginFailureException.class);
    }
}
