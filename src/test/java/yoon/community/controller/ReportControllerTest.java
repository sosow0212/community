package yoon.community.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static yoon.community.factory.UserFactory.createUserWithAdminRole;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Optional;
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
import yoon.community.controller.report.ReportController;
import yoon.community.domain.member.Member;
import yoon.community.dto.report.BoardReportRequest;
import yoon.community.dto.report.MemberReportRequestDto;
import yoon.community.repository.member.MemberRepository;
import yoon.community.service.report.ReportService;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {

    @InjectMocks
    ReportController reportController;

    @Mock
    ReportService reportService;

    @Mock
    MemberRepository memberRepository;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    @Test
    @DisplayName("유저를 신고한다.")
    void report_member_success() throws Exception {
        // given
        MemberReportRequestDto req = new MemberReportRequestDto(1L, "내용");

        Member member = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when, then
        mockMvc.perform(
                        post("/api/reports/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(reportService).reportUser(member, req);
    }

    @Test
    @DisplayName("게시글을 신고한다.")
    void report_board_success() throws Exception {
        // given
        BoardReportRequest req = new BoardReportRequest(1L, "내용");

        Member member = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when, then
        mockMvc.perform(
                        post("/api/reports/boards")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(reportService).reportBoard(member, req);
    }
}
