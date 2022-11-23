package yoon.community.controller.category;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import yoon.community.dto.category.CategoryCreateRequest;
import yoon.community.response.Response;
import yoon.community.service.category.CategoryService;

import javax.validation.Valid;

@Api(value = "Category Controller", tags = "Category")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    @ApiOperation(value = "모든 카테고리 조회", notes = "모든 카테고리를 조회합니다.")
    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllCategories() {
        return Response.success(categoryService.findAll());
    }

    @ApiOperation(value = "카테고리 생성", notes = "카테고리를 생성합니다.")
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createCategory(@Valid @RequestBody CategoryCreateRequest req) {
        categoryService.create(req);
        return Response.success();
    }

    @ApiOperation(value = "카테고리 첫 생성", notes = "카테고리를 처음 생성합니다.")
    @PostMapping("/categories/start")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createCategoryAtFirst() {
        categoryService.createAtFirst();
        return Response.success();
    }

    @ApiOperation(value = "카테고리 삭제", notes = "카테고리를 삭제합니다.")
    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteCategory(@ApiParam(value = "카테고리 id", required = true) @PathVariable int id) {
        categoryService.delete(id);
        return Response.success();
    }

}
