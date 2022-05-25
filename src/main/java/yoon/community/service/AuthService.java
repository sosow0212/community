package yoon.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.config.jwt.TokenProvider;
import yoon.community.dto.sign.LoginRequestDto;
import yoon.community.dto.sign.RegisterDto;
import yoon.community.dto.sign.SignInResponseDto;
import yoon.community.entity.User;
import yoon.community.exception.LoginFailureException;
import yoon.community.exception.MemberNicknameAlreadyExistsException;
import yoon.community.exception.MemberUsernameAlreadyExistsException;
import yoon.community.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    final private TokenProvider tokenProvider;

    @Transactional
    public void register(RegisterDto registerDto) {
        validateSignUpInfo(registerDto);

        User user = new User();
        user.setName(registerDto.getName());
        user.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));
        user.setUsername(registerDto.getUsername());
        user.setNickname(registerDto.getNickname());
        user.setRoles("ROLE_USER");
        userRepository.save(user);
    }

    public SignInResponseDto signIn(LoginRequestDto req) {
        User user = userRepository.findByUsername(req.getUsername()).orElseThrow(() -> {
            return new LoginFailureException();
        });
        validatePassword(req, user);

        String token = tokenProvider.create(user);

        return new SignInResponseDto(token);
    }

    public User find(String username, String password){
        User user = userRepository.findByUsernameAndPassword(username, password);
        System.out.println(user);
        return user;
    }

    private void validateSignUpInfo(RegisterDto registerDto) {
        if(userRepository.existsByUsername(registerDto.getUsername()))
            throw new MemberUsernameAlreadyExistsException(registerDto.getUsername());
        if(userRepository.existsByNickname(registerDto.getNickname()))
            throw new MemberNicknameAlreadyExistsException(registerDto.getNickname());
    }

    private void validatePassword(LoginRequestDto loginRequestDto, User user) {
        if(!bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new LoginFailureException();
        }
    }
}
