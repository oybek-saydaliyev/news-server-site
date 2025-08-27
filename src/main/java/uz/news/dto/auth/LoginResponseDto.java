package uz.news.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private String tokenType = "Bearer";
    private UserResponseDto user;
}
