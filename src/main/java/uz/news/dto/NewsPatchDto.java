package uz.news.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsPatchDto {
    private Long id;
    private String title;
    private String content;
}
