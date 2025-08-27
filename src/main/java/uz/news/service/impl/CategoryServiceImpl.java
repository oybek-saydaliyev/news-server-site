package uz.news.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.news.base.ApiResponse;
import uz.news.dto.CategoryDto;
import uz.news.entity.CategoryEntity;
import uz.news.repository.CategoryRepository;
import uz.news.service.CategoryService;
import uz.news.util.ResMessages;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public ApiResponse<?> create(CategoryDto dto) {
        try{
            CategoryEntity categoryEntity = CategoryDto.toEntity(dto, new CategoryEntity());
            return new ApiResponse<>(200, ResMessages.SUCCESS, categoryRepository.save(categoryEntity));
        }catch (Exception e){
            log.error("Error creating category {}", e.getMessage());
            return new ApiResponse<>(409, "Kategorya yaratishda xatolik !");
        }
    }

    @Override
    public ApiResponse<?> update(Long id, CategoryDto dto) {
        try{
            Optional<CategoryEntity> byId = categoryRepository.findById(id);
            if (byId.isPresent()) {
                CategoryEntity categoryEntity = CategoryDto.toEntity(dto, byId.get());
                return new ApiResponse<>(200, ResMessages.SUCCESS, categoryRepository.save(categoryEntity));
            }
            return new ApiResponse<>(404, "Kategorya topilmadi !");
        }catch (Exception e){
            log.error("Error updating category {}", e.getMessage());
            return new ApiResponse<>(409, "Kategorya topilmadi !");
        }
    }

    @Override
    public ApiResponse<?> getAllAdmin() {
        try{
            return new ApiResponse<>(200, ResMessages.SUCCESS, categoryRepository.findAll());
        }catch (Exception e){
            log.error("Error getting admin category {}", e.getMessage());
            return new ApiResponse<>(409, "Kategorya topilmadi !");
        }
    }

    @Override
    public ApiResponse<?> delete(Long id) {
        try{
            if (categoryRepository.existsById(id)) {
                categoryRepository.deleteById(id);
                return new ApiResponse<>(200, ResMessages.SUCCESS);
            }
            return new ApiResponse<>(404, "Kategorya topilmadi !");
        }catch (Exception e){
            log.error("Error deleting category {}", e.getMessage());
            return new ApiResponse<>(409, "Kategorya topilmadi !");
        }
    }

    @Override
    public ApiResponse<?> getAll() {
        try{
            return new ApiResponse<>(200, ResMessages.SUCCESS, categoryRepository.findAllByIsActive(true));
        }catch (Exception e){
            log.error("Error getting all category {}", e.getMessage());
            return new ApiResponse<>(409, "Kategorya topilmadi !");
        }
    }
}
