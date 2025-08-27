package uz.news.dto.auth;

import lombok.Getter;
import lombok.Setter;
import uz.news.base.BaseDto;
import uz.news.entity.AuthUser;

@Getter
@Setter
public class UserResponseDto extends BaseDto {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Boolean isActive;

    public static UserResponseDto toDto(AuthUser entity, UserResponseDto dto) {
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setFullName(entity.getFullName());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }
}
