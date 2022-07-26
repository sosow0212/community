package yoon.community.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.dto.board.BoardSimpleDto;
import yoon.community.dto.user.UserDto;
import yoon.community.entity.board.Board;
import yoon.community.entity.board.Favorite;
import yoon.community.entity.user.Authority;
import yoon.community.entity.user.User;
import yoon.community.exception.MemberNotEqualsException;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.board.FavoriteRepository;
import yoon.community.repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for(User user : users) {
            userDtos.add(UserDto.toDto(user));
        }
        return userDtos;
    }

    @Transactional(readOnly = true)
    public UserDto findUser(int id) {
        return UserDto.toDto(userRepository.findById(id).orElseThrow(MemberNotFoundException::new));
    }


    @Transactional
    public UserDto editUserInfo(int id, UserDto updateInfo) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            return new MemberNotFoundException();
        });

        // 권한 처리
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!authentication.getName().equals(user.getUsername())) {
            throw new MemberNotEqualsException();
        } else {
            user.setNickname(updateInfo.getNickname());
            user.setName(updateInfo.getName());
            return UserDto.toDto(user);
        }
    }

    @Transactional
    public void deleteUserInfo(int id) {
        User user = userRepository.findById(id).orElseThrow(MemberNotFoundException::new);

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String auth = String.valueOf(authentication.getAuthorities());
        String authByAdmin = "[" + Authority.ROLE_ADMIN +"]";


        if(authentication.getName().equals(user.getUsername()) || auth.equals(authByAdmin)) {
            userRepository.deleteById(id);
        } else {
            throw new MemberNotEqualsException();
        }
    }

    @Transactional(readOnly = true)
    public List<BoardSimpleDto> findFavorites() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        List<Favorite> favorites = favoriteRepository.findAllByUser(user);
        List<Board> boards = new ArrayList<>();

        for(Favorite fav : favorites) {
            boards.add(fav.getBoard());
        }

        List<BoardSimpleDto> boardSimpleDtoList = new ArrayList<>();
        boards.stream().forEach(i -> boardSimpleDtoList.add(new BoardSimpleDto().toDto(i)));
        return boardSimpleDtoList;
    }
}
