package uz.news.dto;

import lombok.Getter;
import lombok.Setter;
import uz.news.base.BaseDto;
import uz.news.entity.NewsEntity;
import uz.news.entity.enums.Status;

/**
 * DTO for {@link uz.news.entity.NewsEntity}
 */
@Getter
@Setter
public class NewsDto extends BaseDto {
    private String title;
    private String content;
    private Status status;
    private Long categoryId;

    public static NewsEntity toEntity(NewsDto dto, NewsEntity entity) {
        entity.setTitle(dto.getTitle() != null ? dto.getTitle() : entity.getTitle());
        entity.setContent(dto.getContent() != null ? dto.getContent() : entity.getContent());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : entity.getStatus());
        return entity;
    }
}