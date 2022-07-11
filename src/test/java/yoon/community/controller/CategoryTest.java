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
import yoon.community.controller.category.CategoryController;
import yoon.community.dto.category.CategoryCreateRequest;
import yoon.community.service.category.CategoryService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CategoryTest {
    @InjectMocks
    CategoryController categoryController;

    @Mock
    CategoryService categoryService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }


    @Test
    @DisplayName("전체 카테고리 조회")
    void readAllTest() throws Exception {
        // given

        // when, then
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
        verify(categoryService).findAll();
    }

    @Test
    @DisplayName("카테고리 생성")
    void createTest() throws Exception {
        // given
        CategoryCreateRequest req = new CategoryCreateRequest("category1", 1);

        // when, then
        mockMvc.perform(
                post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(categoryService).create(req);
    }

    @Test
    @DisplayName("카테고리 제거")
    void deleteTest() throws Exception {
        // given
        int id = 1;

        // when, then
        mockMvc.perform(
                        delete("/api/categories/{id}", id))
                .andExpect(status().isOk());
        verify(categoryService).delete(id);
    }
}
