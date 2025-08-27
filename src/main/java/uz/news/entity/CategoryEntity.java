package uz.news.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import uz.news.base.BaseEntity;

import java.util.List;

@Getter
@Setter
@Entity(name = "category")
public class CategoryEntity extends BaseEntity {
    private String name;
    private Boolean isActive = true;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<NewsEntity> news;
}
