package yoon.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yoon.community.dto.LoginRequestDto;
import yoon.community.dto.RegisterDto;
import yoon.community.service.UserService;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SignTest {
    @InjectMocks UserController userController;
    @Mock UserService signService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper(); // 1

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void signUpTest() throws Exception {
        // given
        RegisterDto req = new RegisterDto("test", "test", "test", "test");

        // when, then
        mockMvc.perform(
                        post("/api/auth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req))) // 2
                .andExpect(status().isCreated());

        verify(signService).register(req);
    }

//    @Test
//    void signInTest() throws Exception {
//        // given
//        LoginRequestDto req = new LoginRequestDto("test", "test");
//        given(signService.signIn(req)).willReturn(new SignInResponse("access", "refresh"));
//
//        // when, then
//        mockMvc.perform(
//                        post("/api/sign-in")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.result.data.accessToken").value("access")) // 3
//                .andExpect(jsonPath("$.result.data.refreshToken").value("refresh"));
//
//        verify(signService).signIn(req);
//    }

    @Test
    void ignoreNullValueInJsonResponseTest() throws Exception { // 4
        // given
        RegisterDto req = new RegisterDto("test", "test", "test", "test");

        // when, then
        mockMvc.perform(
                        post("/api/auth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result").doesNotExist());

    }
}