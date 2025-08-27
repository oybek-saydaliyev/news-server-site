package uz.news.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.news.base.ApiResponse;
import uz.news.dto.NewsDto;
import uz.news.dto.NewsPatchDto;
import uz.news.entity.CategoryEntity;
import uz.news.entity.NewsEntity;
import uz.news.entity.enums.Status;
import uz.news.repository.CategoryRepository;
import uz.news.repository.NewsRepository;
import uz.news.service.NewsService;
import uz.news.util.ResMessages;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ApiResponse<?> create(NewsDto dto) {
        try{
            NewsEntity newsEntity = NewsDto.toEntity(dto, new NewsEntity());
            Optional<CategoryEntity> byId = categoryRepository.findById(dto.getCategoryId());
            if (byId.isPresent()) {
                newsEntity.setCategory(byId.get());
                return new ApiResponse<>(200, ResMessages.SUCCESS, newsRepository.save(newsEntity));
            }
            return new ApiResponse<>(404, "Kategoriya topilmadi !");
        }catch (Exception e){
            log.error("Error creating new news", e);
            return new ApiResponse<>(409, "Yangilik yaratishda xatolik !");
        }
    }

    @Override
    public ApiResponse<?> getOne(Long id) {
        try{
            Optional<NewsEntity> byId = newsRepository.findById(id);
            if (byId.isPresent()) {
                return new ApiResponse<>(200, ResMessages.SUCCESS, byId.get());
            }
            return new ApiResponse<>(404, "Yangilik topilmadi !");
        }catch (Exception e){
            log.error("Error getting news", e);
            return new ApiResponse<>(409, ResMessages.SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<?> getOnePublished(Long id) {
        try {
            Optional<NewsEntity> byIdAndStatusPublished = newsRepository.findByIdAndStatusPublished(id, Status.PUBLISHED);
            if (byIdAndStatusPublished.isPresent()) {
                return new ApiResponse<>(200, ResMessages.SUCCESS, byIdAndStatusPublished.get());
            }
            return new ApiResponse<>(404, "Yangilik topilmadi !");
        }catch (Exception e){
            log.error("Error getting news", e);
            return new ApiResponse<>(409, ResMessages.SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<?> getAllAdmin(Status status, Long authorId, Long categoryId) {
        try{
            List<NewsEntity> allByStatusAndCreatedByAndCategoryId = newsRepository.findAllByStatusAndCreatedByAndCategory_Id(status, authorId, categoryId);
            return new ApiResponse<>(200, ResMessages.SUCCESS, allByStatusAndCreatedByAndCategoryId);
        }catch (Exception e){
            log.error("Error getting news", e);
            return new ApiResponse<>(409, ResMessages.SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<?> getAll(String search, Long categoryId, LocalDateTime from, LocalDateTime to) {
        try {
            List<NewsEntity> all = newsRepository.findAllBySearchAndCategoryIdAndBetweenDate(search, categoryId, from, to);
            return new ApiResponse<>(200, ResMessages.SUCCESS, all);
        }catch (Exception e){
            log.error("Error getting news", e);
            return new ApiResponse<>(409, ResMessages.SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<?> update(Long id, NewsDto dto) {
        try{
            Optional<NewsEntity> byId = newsRepository.findById(id);
            if (byId.isPresent()) {
                NewsEntity newsEntity = NewsDto.toEntity(dto, byId.get());
                Optional<CategoryEntity> byCategoryId = categoryRepository.findById(dto.getCategoryId());
                if (byCategoryId.isPresent()) {
                    newsEntity.setCategory(byCategoryId.get());
                    return new ApiResponse<>(200, ResMessages.SUCCESS, newsRepository.save(newsEntity));
                }
                return new ApiResponse<>(404, "Kategoriya topilmadi !");
            }
            return new ApiResponse<>(404, "Yangilik topilmadi !");
        }catch (Exception e){
            log.error("Error updating news", e);
            return new ApiResponse<>(409, ResMessages.SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<?> updateContent(Long id, NewsPatchDto patchDto) {
        try{
            Optional<NewsEntity> byId = newsRepository.findById(patchDto.getId());
            if (byId.isPresent()) {
                NewsEntity newsEntity = byId.get();
                if (patchDto.getTitle() != null) {
                    newsEntity.setTitle(patchDto.getTitle());
                }
                if (patchDto.getContent() != null) {
                    newsEntity.setContent(patchDto.getContent());
                }
                return new ApiResponse<>(200, ResMessages.SUCCESS, newsRepository.save(newsEntity));
            }
            return new ApiResponse<>(404, "Yangilik topilmadi !");
        }catch (Exception e){
            log.error("Error updating content", e);
            return new ApiResponse<>(409, ResMessages.SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<?> updateStatus(Long id, Status status) {
        try{
            Optional<NewsEntity> byId = newsRepository.findById(id);
            if (byId.isPresent()) {
                NewsEntity newsEntity = byId.get();
                newsEntity.setStatus(status);
                return new ApiResponse<>(200, ResMessages.SUCCESS, newsRepository.save(newsEntity));
            }
            return new ApiResponse<>(404, "Yangilik topilmadi !");
        }catch (Exception e){
            log.error("Error updating status", e);
            return new ApiResponse<>(409, ResMessages.SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<?> restore(Long id) {
        try{
            Optional<NewsEntity> byId = newsRepository.findById(id);
            if (byId.isPresent()) {
                NewsEntity newsEntity = byId.get();
                newsEntity.setDeleted(false);
                return new ApiResponse<>(200, ResMessages.SUCCESS, newsRepository.save(newsEntity));
            }
            return new ApiResponse<>(404, "Yangilik topilmadi !");
        }catch (Exception e){
            log.error("Error restoring news", e);
            return new ApiResponse<>(409, ResMessages.SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<?> deleteSoft(Long id) {
        try{
            Optional<NewsEntity> byId = newsRepository.findById(id);
            if (byId.isPresent()) {
                NewsEntity newsEntity = byId.get();
                newsEntity.setDeleted(true);
                return new ApiResponse<>(200, ResMessages.SUCCESS, newsRepository.save(newsEntity));
            }
            return new ApiResponse<>(404, "Yangilik topilmadi !");
        }catch (Exception e){
            log.error("Error deleting news", e);
            return new ApiResponse<>(409, ResMessages.SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<?> deleteHard(Long id) {
        try{
            if (!newsRepository.existsById(id)) {
                return new ApiResponse<>("Yangilik topilmadi !", 404);
            }
            newsRepository.deleteById(id);
            return new ApiResponse<>(200, ResMessages.SUCCESS);
        }catch (Exception e){
            log.error("Error deleting news", e);
            return new ApiResponse<>(409, ResMessages.SERVER_ERROR);
        }
    }
}
