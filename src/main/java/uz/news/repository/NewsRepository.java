package uz.news.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.news.base.BaseRepository;
import uz.news.entity.NewsEntity;
import uz.news.entity.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NewsRepository extends BaseRepository<NewsEntity> {
    List<NewsEntity> findAllByStatusAndCreatedByAndCategory_Id(Status status, Long createdBy, Long categoryId);

    @Query("""
            SELECT n FROM news n 
            WHERE (:categoryId IS NULL OR n.category.id = :categoryId) 
            AND (:search IS NULL OR LOWER(n.title) LIKE LOWER(CONCAT('%', :search, '%')) 
            OR LOWER(n.content) LIKE LOWER(CONCAT('%', :search, '%'))) 
            AND (n.createdAt BETWEEN :fromDate AND :toDate) 
            AND n.status = 'PUBLISHED' 
            AND n.deleted = false""")
    List<NewsEntity> findAllBySearchAndCategoryIdAndBetweenDate(@Param("search") String search,
                                                                @Param("categoryId") Long categoryId,
                                                                @Param("fromDate") LocalDateTime from,
                                                                @Param("toDate") LocalDateTime to);


    @Query("select news from news where status =:status and id =:id")
    Optional<NewsEntity> findByIdAndStatusPublished(@Param("id") Long id,
                                                    @Param("status") Status status);
}