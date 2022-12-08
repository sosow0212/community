package yoon.community.service.user;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.dto.board.BoardSimpleDto;
import yoon.community.dto.user.UserEditRequestDto;
import yoon.community.entity.board.Board;
import yoon.community.entity.board.Favorite;
import yoon.community.entity.user.RefreshToken;
import yoon.community.entity.user.User;
import yoon.community.exception.MemberNotEqualsException;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.repository.board.FavoriteRepository;
import yoon.community.repository.refreshToken.RefreshTokenRepository;
import yoon.community.repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional(readOnly = true)
    public List<UserEditRequestDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserEditRequestDto> result = users.stream()
                .map(user -> UserEditRequestDto.toDto(user))
                .collect(Collectors.toList());
        return result;
    }

    @Transactional(readOnly = true)
    public UserEditRequestDto findUser(int id) {
        return UserEditRequestDto.toDto(userRepository.findById(id).orElseThrow(MemberNotFoundException::new));
    }


    @Transactional
    public User editUserInfo(User user, UserEditRequestDto userEditRequestDto) {
        // refreshToken 처리는 어떻게 할지 추후에 고민해보기
        user.editUser(userEditRequestDto);
        return user;
    }

    @Transactional
    public void deleteUserInfo(User user) {
        // jwt 토큰 만료 처리는 어떻게 할지 추후에 고민해보기
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<BoardSimpleDto> findFavorites(User user) {
        List<Favorite> favorites = favoriteRepository.findAllByUser(user);
        List<BoardSimpleDto> boardSimpleDtoList = new ArrayList<>();
        favorites.stream()
                .map(favorite -> boardSimpleDtoList.add(new BoardSimpleDto().toDto(favorite.getBoard())))
                .collect(Collectors.toList());
        return boardSimpleDtoList;
    }
}
