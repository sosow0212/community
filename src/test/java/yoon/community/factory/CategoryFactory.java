package yoon.community.factory;

import yoon.community.entity.category.Category;

public class CategoryFactory {
    public static Category createCategory() {
        return new Category("name", null);
    }
}
