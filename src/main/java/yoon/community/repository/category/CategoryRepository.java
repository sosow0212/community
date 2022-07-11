package yoon.community.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yoon.community.entity.category.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category c LEFT JOIN c.parent p ORDER BY p.id ASC NULLS FIRST, c.id ASC")
    List<Category> findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
}
