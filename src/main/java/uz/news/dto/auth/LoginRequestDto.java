package uz.news.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotNull
public class LoginRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
