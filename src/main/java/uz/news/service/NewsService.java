package uz.news.service;

import uz.news.base.ApiResponse;
import uz.news.dto.NewsDto;
import uz.news.dto.NewsPatchDto;
import uz.news.entity.enums.Status;

import java.time.LocalDateTime;

public interface NewsService {
    ApiResponse<?> create(NewsDto dto);
    ApiResponse<?> getOne(Long id);
    ApiResponse<?> getOnePublished(Long id);
    ApiResponse<?> getAllAdmin(Status status, Long authorId, Long categoryId);
    ApiResponse<?> getAll(String search, Long categoryId, LocalDateTime from, LocalDateTime to);
    ApiResponse<?> update(Long id, NewsDto dto);
    ApiResponse<?> updateContent(Long id, NewsPatchDto patchDto);
    ApiResponse<?> updateStatus(Long id, Status status);
    ApiResponse<?> restore(Long id);
    ApiResponse<?> deleteSoft(Long id);
    ApiResponse<?> deleteHard(Long id);
}
