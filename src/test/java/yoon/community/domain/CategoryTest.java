package yoon.community.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import yoon.community.domain.category.Category;

public class CategoryTest {
    @Test
    @DisplayName("카테고리 생성자 테스트")
    public void categoryConstructorTest() {

        // given, when
        String name = "카테고리";
        Category category = new Category(name, null);

        // then
        assertThat(category.getName()).isEqualTo(name);
    }
}
