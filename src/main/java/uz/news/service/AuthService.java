package uz.news.service;

import uz.news.base.ApiResponse;
import uz.news.dto.auth.LoginRequestDto;

public interface AuthService {
    ApiResponse<?> login(LoginRequestDto loginRequestDto);
    ApiResponse<?> refreshToken(String token);
    ApiResponse<?> getMe();
}
