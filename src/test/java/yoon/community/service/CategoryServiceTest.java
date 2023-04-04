package yoon.community.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static yoon.community.factory.CategoryFactory.createCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yoon.community.domain.category.Category;
import yoon.community.dto.category.CategoryCreateRequest;
import yoon.community.dto.category.CategoryDto;
import yoon.community.exception.CategoryNotFoundException;
import yoon.community.repository.category.CategoryRepository;
import yoon.community.repository.member.MemberRepository;
import yoon.community.service.category.CategoryService;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("카테고리를 모두 찾는다.")
    void find_categories_success() {
        // given
        List<Category> categories = new ArrayList<>();
        categories.add(createCategory());
        given(categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc()).willReturn(categories);

        // when
        List<CategoryDto> result = categoryService.findAllCategory();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("카테고리 생성을 한다.")
    void create_category_success() {
        // given
        CategoryCreateRequest req = new CategoryCreateRequest("name", 1);
        given(categoryRepository.findById(anyInt())).willReturn(Optional.of(createCategory()));

        // when
        categoryService.createCategory(req);

        // then
        verify(categoryRepository).save(any());
    }

    @Test
    @DisplayName("부모 카테고리의 id가 잘못되면, 카테고리 생성에 실패한다.")
    void throws_exception_when_category_creating_with_invalid_parent_category_id() {
        // given
        CategoryCreateRequest req = new CategoryCreateRequest("name", 1);

        // when & then
        assertThatThrownBy(() -> categoryService.createCategory(req))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("카테고리를 지운다.")
    void delete_category_success() {
        // given
        given(categoryRepository.findById(anyInt())).willReturn(Optional.of(createCategory()));

        // when
        categoryService.deleteCategory(anyInt());

        // then
        verify(categoryRepository).delete(any());
    }
}
