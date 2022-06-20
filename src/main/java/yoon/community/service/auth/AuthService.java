package yoon.community.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.config.jwt.TokenProvider;
import yoon.community.dto.sign.*;
import yoon.community.entity.user.Authority;
import yoon.community.entity.user.RefreshToken;
import yoon.community.entity.user.User;
import yoon.community.exception.LoginFailureException;
import yoon.community.exception.MemberNicknameAlreadyExistsException;
import yoon.community.exception.MemberUsernameAlreadyExistsException;
import yoon.community.repository.refreshToken.RefreshTokenRepository;
import yoon.community.repository.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void signup(SignUpRequestDto req) {
        validateSignUpInfo(req);

        // Builder로 리팩토링 해야함
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setNickname(req.getNickname());
        user.setName(req.getName());
        user.setAuthority(Authority.ROLE_USER);
        userRepository.save(user);
    }


    @Transactional
    public TokenResponseDto signIn(LoginRequestDto req) {
        User user = userRepository.findByUsername(req.getUsername()).orElseThrow(() -> {
            return new LoginFailureException();
        });

        validatePassword(req, user);

        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = req.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        TokenResponseDto tokenResponseDto = new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        // 5. 토큰 발급
        return tokenResponseDto;
    }


    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        TokenResponseDto tokenResponseDto = new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        return tokenResponseDto;
    }


    private void validateSignUpInfo(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByUsername(signUpRequestDto.getUsername()))
            throw new MemberUsernameAlreadyExistsException(signUpRequestDto.getUsername());
        if (userRepository.existsByNickname(signUpRequestDto.getNickname()))
            throw new MemberNicknameAlreadyExistsException(signUpRequestDto.getNickname());
    }

    private void validatePassword(LoginRequestDto loginRequestDto, User user) {
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new LoginFailureException();
        }
    }

}
