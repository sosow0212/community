package yoon.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import yoon.community.controller.board.BoardController;
import yoon.community.dto.board.BoardCreateRequest;
import yoon.community.dto.board.BoardUpdateRequest;
import yoon.community.entity.board.Board;
import yoon.community.repository.board.BoardRepository;
import yoon.community.service.board.BoardService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {
    @InjectMocks
    BoardController boardController;

    @Mock
    BoardService boardService;
    @Mock
    BoardRepository boardRepository;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
    }

    @Test
    @DisplayName("게시글 작성")
    public void createTest() throws Exception {
        // given
        ArgumentCaptor<BoardCreateRequest> boardCreateRequestArgumentCaptor = ArgumentCaptor.forClass(BoardCreateRequest.class);
        List<MultipartFile> images = new ArrayList<>();
        images.add(new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()));
        images.add(new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes()));
        BoardCreateRequest req = new BoardCreateRequest("title", "content", images);

        // when, then
        mockMvc.perform(
                        multipart("/api/boards")
                                .file("images", images.get(0).getBytes())
                                .file("images", images.get(1).getBytes())
                                .param("title", req.getTitle())
                                .param("content", req.getContent())
                                .with(requestBoardProcessor -> {
                                    requestBoardProcessor.setMethod("POST");
                                    return requestBoardProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());

        verify(boardService).create(boardCreateRequestArgumentCaptor.capture());

        BoardCreateRequest capturedReq = boardCreateRequestArgumentCaptor.getValue();
        assertThat(capturedReq.getImages().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시물 검색")
    public void searchTest() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<Board> result = boardRepository.findByTitleContaining("keyword", pageable);

        // when, then
        assertThat(result).isEqualTo(new ArrayList<>());

    }


    @Test
    @DisplayName("전체 게시물 조회 (페이징)")
    public void findAllBoardsTest() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        Page<Board> result = boardRepository.findAll(pageable);

        // when, then
        assertThat(result).isEqualTo(null);


    }


    @Test
    @DisplayName("개시물 단건 조회")
    public void findBoardTest() throws Exception {
        // given
        int id = 1;

        // when, then
        mockMvc.perform(
                        get("/api/boards/{id}", id))
                .andExpect(status().isOk());

        verify(boardService).findBoard(id);
    }

    @Test
    @DisplayName("게시글 수정")
    public void editBoardTest() throws Exception {
        // given
        ArgumentCaptor<BoardUpdateRequest> boardUpdateRequestArgumentCaptor = ArgumentCaptor.forClass(BoardUpdateRequest.class);

        List<MultipartFile> addedImages = new ArrayList<>();
        addedImages.add(new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()));
        addedImages.add(new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes()));

        List<Integer> deletedImages = List.of(1, 2);

        BoardUpdateRequest req = new BoardUpdateRequest("title", "content", addedImages, deletedImages);

        // when, then
        mockMvc.perform(
                        multipart("/api/boards/{id}", 1)
                                .file("addedImages", addedImages.get(0).getBytes())
                                .file("addedImages", addedImages.get(1).getBytes())
                                .param("deletedImages", String.valueOf(deletedImages.get(0)), String.valueOf(deletedImages.get(1)))
                                .param("title", req.getTitle())
                                .param("content", req.getContent())
                                .with(requestBoardProcessor -> {
                                    requestBoardProcessor.setMethod("PUT");
                                    return requestBoardProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(boardService).editBoard(anyInt(), boardUpdateRequestArgumentCaptor.capture());

        BoardUpdateRequest capturedReq = boardUpdateRequestArgumentCaptor.getValue();
        List<MultipartFile> capturedAddedImages = capturedReq.getAddedImages();
        assertThat(capturedAddedImages.size()).isEqualTo(2);

        List<Integer> capturedDeletedImages = capturedReq.getDeletedImages();
        Assertions.assertThat(capturedDeletedImages.size()).isEqualTo(2);
        Assertions.assertThat(capturedDeletedImages).contains(deletedImages.get(0), deletedImages.get(1));
    }
}

