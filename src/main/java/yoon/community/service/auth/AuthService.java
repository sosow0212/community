package yoon.community.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.config.constant.Constant;
import yoon.community.config.jwt.TokenProvider;
import yoon.community.domain.member.Authority;
import yoon.community.domain.member.Member;
import yoon.community.domain.member.RefreshToken;
import yoon.community.domain.point.Point;
import yoon.community.dto.sign.LoginRequestDto;
import yoon.community.dto.sign.SignUpRequestDto;
import yoon.community.dto.sign.TokenDto;
import yoon.community.dto.sign.TokenRequestDto;
import yoon.community.dto.sign.TokenResponseDto;
import yoon.community.exception.LoginFailureException;
import yoon.community.exception.MemberNicknameAlreadyExistsException;
import yoon.community.exception.UsernameAlreadyExistsException;
import yoon.community.repository.member.MemberRepository;
import yoon.community.repository.point.PointRepository;
import yoon.community.repository.refreshToken.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String RANKING_KEY = Constant.REDIS_RANKING_KEY;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenProvider tokenProvider;

    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public Member signup(SignUpRequestDto req) {
        validateSignUpInfo(req);
        Member member = createSignupFormOfUser(req);
        memberRepository.save(member);
        return member;
    }

    @Transactional
    public void savePointEntity(Member member) {
        Point point = new Point(member);
        pointRepository.save(point);
        redisTemplate.opsForZSet().add(RANKING_KEY, member.getUsername(), point.getPoint());
    }

    @Transactional
    public TokenResponseDto signIn(LoginRequestDto req) {
        Member member = memberRepository.findByUsername(req.getUsername()).orElseThrow(() -> {
            throw new LoginFailureException();
        });

        validatePassword(req, member);
        Authentication authentication = getUserAuthentication(req);
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken refreshToken = buildRefreshToken(authentication, tokenDto);
        refreshTokenRepository.save(refreshToken);
        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }

    private RefreshToken buildRefreshToken(Authentication authentication, TokenDto tokenDto) {
        return RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
    }

    private Authentication getUserAuthentication(LoginRequestDto req) {
        UsernamePasswordAuthenticationToken authenticationToken = req.toAuthentication();
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }

    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
        validateRefreshToken(tokenRequestDto);

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        validateRefreshTokenOwner(refreshToken, tokenRequestDto);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }

    private Member createSignupFormOfUser(SignUpRequestDto req) {
        return Member.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .nickname(req.getNickname())
                .name(req.getName())
                .authority(Authority.ROLE_USER)
                .build();
    }

    private void validateSignUpInfo(SignUpRequestDto signUpRequestDto) {
        if (memberRepository.existsByUsername(signUpRequestDto.getUsername())) {
            throw new UsernameAlreadyExistsException(signUpRequestDto.getUsername());
        }

        if (memberRepository.existsByNickname(signUpRequestDto.getNickname())) {
            throw new MemberNicknameAlreadyExistsException(signUpRequestDto.getNickname());
        }
    }

    private void validatePassword(LoginRequestDto loginRequestDto, Member member) {
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new LoginFailureException();
        }
    }

    private void validateRefreshToken(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }
    }

    private void validateRefreshTokenOwner(RefreshToken refreshToken, TokenRequestDto tokenRequestDto) {
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
    }
}
