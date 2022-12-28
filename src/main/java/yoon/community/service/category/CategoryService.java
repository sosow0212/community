package yoon.community.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.dto.category.CategoryCreateRequest;
import yoon.community.dto.category.CategoryDto;
import yoon.community.entity.category.Category;
import yoon.community.entity.user.User;
import yoon.community.exception.CategoryNotFoundException;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.repository.category.CategoryRepository;
import yoon.community.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final static String DEFAULT_CATEGORY = "Default";
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        List<Category> categories = categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
        return CategoryDto.toDtoList(categories);
    }

    @Transactional
    public void createAtFirst() {
        Category category = new Category(DEFAULT_CATEGORY, null);
        categoryRepository.save(category);
    }

    @Transactional
    public void create(CategoryCreateRequest req) {
        Category parent = Optional.ofNullable(req.getParentId())
                .map(id -> categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new))
                .orElse(null);
        categoryRepository.save(new Category(req.getName(), parent));
    }

    @Transactional
    public void delete(int id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(category);
    }
}
