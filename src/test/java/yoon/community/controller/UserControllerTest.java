package yoon.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yoon.community.controller.user.UserController;
import yoon.community.dto.user.UserEditRequestDto;
import yoon.community.entity.user.User;
import yoon.community.repository.user.UserRepository;
import yoon.community.service.user.UserService;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static yoon.community.factory.UserFactory.createUserWithAdminRole;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    @Mock
    UserService userService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("전체 회원 조회")
    public void findAllUsersTest() throws Exception {
        mockMvc.perform(
                        get("/api/users"))
                .andExpect(status().isOk());
        verify(userService).findAllUsers();
    }

    @Test
    @DisplayName("개별 회원 조회")
    public void findUserTest() throws Exception {
        //given
        int id = 1;

        //when, then
        mockMvc.perform(
                        get("/api/users/{id}", id))
                .andExpect(status().isOk());
        verify(userService).findUser(id);
    }

    @Test
    @DisplayName("즐겨찾기 조회")
    public void findFavoritesTest() throws Exception {
        // given
        User user = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(userRepository.findByUsername(authentication.getName())).willReturn(Optional.of(user));

        // when, then
        mockMvc.perform(
                        get("/api/users/favorites"))
                .andExpect(status().isOk());
        verify(userService).findFavorites(user);
    }

    @Test
    @DisplayName("회원 정보 수정")
    public void editUserInfoTest() throws Exception {
        // given
        UserEditRequestDto userEditRequestDto = new UserEditRequestDto("name", "nickname");
        User user = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(userRepository.findByUsername(authentication.getName())).willReturn(Optional.of(user));

        // when, then
        mockMvc.perform(
                        put("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userEditRequestDto)))
                .andExpect(status().isOk());

        verify(userService).editUserInfo(user, userEditRequestDto);
        assertThat(userEditRequestDto.getName()).isEqualTo("name");

    }

    @Test
    @DisplayName("회원 탈퇴")
    public void deleteUserInfoTest() throws Exception {
        // given
        User user = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(userRepository.findByUsername(authentication.getName())).willReturn(Optional.of(user));

        // when then
        mockMvc.perform(
                        delete("/api/users"))
                .andExpect(status().isOk());

        verify(userService).deleteUserInfo(user);

    }
}
