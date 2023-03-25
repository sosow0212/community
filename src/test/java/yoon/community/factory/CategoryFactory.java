package yoon.community.factory;

import yoon.community.domain.category.Category;

public class CategoryFactory {

    public static Category createCategory() {
        return new Category("name", null);
    }
}
