package yoon.community.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import yoon.community.controller.category.CategoryController;
import yoon.community.dto.category.CategoryCreateRequest;
import yoon.community.service.category.CategoryService;

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
    @DisplayName("전체 카테고리를 조회한다.")
    void find_categories_success() throws Exception {
        // when, then
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
        verify(categoryService).findAllCategory();
    }

    @Test
    @DisplayName("카테고리를 생성한다.")
    void create_category_success() throws Exception {
        // given
        CategoryCreateRequest req = new CategoryCreateRequest("category1", 1);

        // when, then
        mockMvc.perform(
                        post("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(categoryService).createCategory(req);
    }

    @Test
    @DisplayName("카테고리를 제거한다.")
    void delete_category_success() throws Exception {
        // given
        int id = 1;

        // when, then
        mockMvc.perform(
                        delete("/api/categories/{id}", id))
                .andExpect(status().isOk());
        verify(categoryService).deleteCategory(id);
    }
}
