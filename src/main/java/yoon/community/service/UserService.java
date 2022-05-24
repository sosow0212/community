package yoon.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.dto.sign.LoginRequestDto;
import yoon.community.dto.sign.RegisterDto;
import yoon.community.entity.User;
import yoon.community.exception.LoginFailureException;
import yoon.community.exception.MemberNicknameAlreadyExistsException;
import yoon.community.exception.MemberUsernameAlreadyExistsException;
import yoon.community.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
