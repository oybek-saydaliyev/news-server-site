package uz.news.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.news.base.ApiResponse;
import uz.news.dto.CategoryDto;
import uz.news.service.CategoryService;

@RestController("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto dto) {
        return ApiResponse.controller(categoryService.create(dto));
    }

    @GetMapping("/admin/categories")
    public ResponseEntity<?> getAllCategories() {
        return ApiResponse.controller(categoryService.getAllAdmin());
    }

    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDto dto) {
        return ApiResponse.controller(categoryService.update(id, dto));
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        return ApiResponse.controller(categoryService.delete(id));
    }

    //public
    @GetMapping("/public/categories")
    public ResponseEntity<?> getAllPublicCategories() {
        return ApiResponse.controller(categoryService.getAll());
    }
}
