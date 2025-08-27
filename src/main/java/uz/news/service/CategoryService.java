package uz.news.service;

import uz.news.base.ApiResponse;
import uz.news.dto.CategoryDto;

public interface CategoryService {
    ApiResponse<?> create(CategoryDto dto);
    ApiResponse<?> update(Long id, CategoryDto dto);
    ApiResponse<?> getAllAdmin();
    ApiResponse<?> delete(Long id);
    ApiResponse<?> getAll();
}
