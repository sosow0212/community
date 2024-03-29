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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yoon.community.config.guard.LoginMemberArgumentResolver;
import yoon.community.controller.comment.CommentController;
import yoon.community.domain.member.Member;
import yoon.community.dto.comment.CommentCreateRequest;
import yoon.community.dto.comment.CommentReadCondition;
import yoon.community.service.comment.CommentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static yoon.community.factory.UserFactory.createUserWithAdminRole;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @InjectMocks
    CommentController commentController;

    @Mock
    CommentService commentService;

    @Mock
    LoginMemberArgumentResolver loginMemberArgumentResolver;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setCustomArgumentResolvers(loginMemberArgumentResolver)
                .build();
    }

    @Test
    @DisplayName("댓글 작성을 한다.")
    void create_comment_success() throws Exception {
        // given
        CommentCreateRequest req = new CommentCreateRequest(1L, "content");
        Member member = createUserWithAdminRole();

        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        //when then
        mockMvc.perform(
                        post("/api/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(commentService).createComment(req, member);
    }

    @Test
    @DisplayName("모든 댓글을 조회한다.")
    void find_all_comments_success() throws Exception {
        // given
        CommentReadCondition commentReadCondition = new CommentReadCondition(1L);

        // when, then
        mockMvc.perform(
                        get("/api/comments")
                                .param("boardId", String.valueOf(commentReadCondition.getBoardId()))
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(commentService).findAllComments(commentReadCondition);
    }

    @Test
    @DisplayName("댓글 삭제를 한다.")
    void delete_comment_success() throws Exception {
        // given
        Long id = 1L;
        Member member = createUserWithAdminRole();

        given(loginMemberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        // when, then
        mockMvc.perform(
                        delete("/api/comments/{id}", id))
                .andExpect(status().isOk());
        verify(commentService).deleteComment(id, member);
    }
}
