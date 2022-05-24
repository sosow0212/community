package yoon.community.learning;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import yoon.community.response.Response;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class WebMvcTest {

    @InjectMocks TestController testController;
    MockMvc mockMvc; // 컨트롤러 요청을 위해 MockMvc 사용

    @Controller // 테스트 컨트롤러
    public static class TestController {
        @GetMapping("/test/ignore-null-value")
        public Response ignoreNullValueTest() {
            return Response.success();
        }
    }


    @BeforeEach
    void beforeEach() {
        // Mockito를 이용하여 TestController 띄우기, 이제 MockMvc로 컨트롤러에 요청을 보내서 테스트 가능
        mockMvc = MockMvcBuilders.standaloneSetup(testController).build();
    }

    @Test
    void ignoreNullValueInJsonResponseTest() throws Exception {
        mockMvc.perform(
                // MockMvc.perform으로 요청을 보내고, 그 결과를 검증할 수 있습니다.
                // "/test/ignore-null-value"로 get 요청을 보낸 뒤,
                // 응답 상태 코드 200과 응답 JSON에 result 필드가 없음을 확인하였습니다.
                get("/test/ignore-null-value"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").doesNotExist());
    }
}