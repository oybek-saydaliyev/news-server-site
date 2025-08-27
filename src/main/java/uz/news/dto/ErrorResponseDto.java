package uz.news.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponseDto {
    private int code;
    private String message;
    private LocalDateTime timestamp;
    private String path;
}
