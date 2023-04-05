package yoon.community.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import yoon.community.domain.category.Category;

public class CategoryTest {

    @Test
    @DisplayName("카테고리 생성에 성공한다.")
    void create_category_success() {
        // given, when
        String name = "카테고리";
        Category category = new Category(name, null);

        // then
        assertThat(category.getName()).isEqualTo(name);
    }
}
