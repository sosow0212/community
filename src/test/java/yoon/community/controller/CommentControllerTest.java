package yoon.community.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import yoon.community.controller.comment.CommentController;
import yoon.community.domain.member.Member;
import yoon.community.dto.comment.CommentCreateRequest;
import yoon.community.dto.comment.CommentReadCondition;
import yoon.community.repository.member.MemberRepository;
import yoon.community.service.comment.CommentService;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @InjectMocks
    CommentController commentController;

    @Mock
    MemberRepository memberRepository;

    @Mock
    CommentService commentService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    @DisplayName("댓글 작성을 한다.")
    public void create_comment_success() throws Exception {
        // given
        CommentCreateRequest req = new CommentCreateRequest(1L, "content");

        Member member = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

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
    public void find_all_comments_success() throws Exception {
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
    public void delete_comment_success() throws Exception {
        // given
        Long id = 1L;

        Member member = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        // when, then
        mockMvc.perform(
                        delete("/api/comments/{id}", id))
                .andExpect(status().isOk());
        verify(commentService).deleteComment(id, member);
    }
}
