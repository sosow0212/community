package yoon.community.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yoon.community.controller.auth.AuthController;
import yoon.community.dto.sign.LoginRequestDto;
import yoon.community.dto.sign.SignUpRequestDto;
import yoon.community.dto.sign.TokenResponseDto;
import yoon.community.service.auth.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    AuthController authController;
    @Mock
    AuthService authService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @DisplayName("회원가입을 한다.")
    @Test
    void sign_up_success() throws Exception {
        // given
        SignUpRequestDto req = new SignUpRequestDto("test123", "test", "username", "nickname");

        // when, then
        mockMvc.perform(
                        post("/api/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(authService).signup(req);
    }

    @DisplayName("로그인 테스트")
    @Test
    void sign_in_success() throws Exception {
        // given
        LoginRequestDto req = new LoginRequestDto("test123", "test");
        given(authService.signIn(req)).willReturn(new TokenResponseDto("access", "refresh"));

        // when, then
        mockMvc.perform(
                        post("/api/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.data.accessToken").value("access"))
                .andExpect(jsonPath("$.result.data.refreshToken").value("refresh"));

        verify(authService).signIn(req);
    }

    @DisplayName("응답 JSON 테스트")
    @Test
    void ignore_null_value_in_json_response_success() throws Exception {
        // 응답결과로 반환되는 JSON 문자열이 올바르게 제거되는지 검증
        // given
        SignUpRequestDto req = new SignUpRequestDto("test123", "test", "username", "nickname");

        // when, then
        mockMvc.perform(
                        post("/api/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result").doesNotExist());
    }
}
