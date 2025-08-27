package uz.news.repository;

import uz.news.base.BaseRepository;
import uz.news.entity.CategoryEntity;

import java.util.List;

public interface CategoryRepository extends BaseRepository<CategoryEntity> {
    List<CategoryEntity> findAllByIsActive(Boolean isActive);
}