package yoon.community.service.category;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.domain.category.Category;
import yoon.community.dto.category.CategoryCreateRequest;
import yoon.community.dto.category.CategoryDto;
import yoon.community.exception.CategoryNotFoundException;
import yoon.community.repository.category.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    private final static String DEFAULT_CATEGORY = "Default";

    private final CategoryRepository categoryRepository;

    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findAllCategory() {
        List<Category> categories = categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
        return CategoryDto.toDtoList(categories);
    }

    @Transactional
    public void createAtFirst() {
        Category category = new Category(DEFAULT_CATEGORY, null);
        categoryRepository.save(category);
    }

    @Transactional
    public void createCategory(final CategoryCreateRequest req) {
        Category parent = categoryRepository.findById(req.getParentId())
                .orElseThrow(CategoryNotFoundException::new);

        categoryRepository.save(new Category(req.getName(), parent));
    }

    @Transactional
    public void deleteCategory(final int id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(category);
    }
}
