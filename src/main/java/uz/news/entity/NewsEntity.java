package uz.news.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.news.base.BaseEntity;
import uz.news.entity.enums.Status;

@Getter
@Setter
@Entity(name = "news")
public class NewsEntity extends BaseEntity {
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
}
