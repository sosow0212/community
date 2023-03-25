package yoon.community.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yoon.community.controller.point.PointController;
import yoon.community.service.point.PointService;

@ExtendWith(MockitoExtension.class)
public class PointControllerTest {

    @InjectMocks
    PointController pointController;

    @Mock
    PointService pointService;

    MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(pointController).build();
    }

    @Test
    @DisplayName("포인트 랭킹 조회")
    public void findPointsRankingTest() throws Exception {
        // given

        // when
        mockMvc.perform(
                        get("/api/points"))
                .andExpect(status().isOk());

        // then
        verify(pointService).findPointsRankingWithRedis();
    }
}
