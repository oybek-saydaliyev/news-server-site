package uz.news.dto;

import lombok.Getter;
import lombok.Setter;
import uz.news.entity.CategoryEntity;

import java.io.Serializable;

/**
 * DTO for {@link uz.news.entity.CategoryEntity}
 */
@Getter
@Setter
public class CategoryDto implements Serializable {
    private String name;
    private Boolean isActive = true;

    public static CategoryEntity toEntity(CategoryDto dto, CategoryEntity entity) {
        entity.setName(dto.getName() != null ? dto.getName() : entity.getName());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }
}