package uz.news.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.news.base.ApiResponse;
import uz.news.dto.auth.LoginRequestDto;
import uz.news.dto.auth.LoginResponseDto;
import uz.news.dto.auth.UserResponseDto;
import uz.news.entity.AuthUser;
import uz.news.repository.UserRepository;
import uz.news.security.JwtUtil;
import uz.news.security.SessionUser;
import uz.news.service.AuthService;
import uz.news.util.ResMessages;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public ApiResponse<?> login(LoginRequestDto loginRequestDto) {
        try {
            Optional<AuthUser> userOptional = userRepository.findByUsername(loginRequestDto.getUsername());

            if (userOptional.isEmpty()) {
                return new ApiResponse<>(ResMessages.USER_NOT_FOUND, 404);
            }

            AuthUser user = userOptional.get();

            if (!user.getIsActive()) {
                return new ApiResponse<>(ResMessages.USER_NOT_ACTIVE, 403);
            }

            if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
                return new ApiResponse<>(ResMessages.INVALID_PASSWORD, 401);
            }

            String accessToken = jwtUtil.generateAccessToken(user.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

            LoginResponseDto response = LoginResponseDto.builder()
                    .user(UserResponseDto.toDto(user, new UserResponseDto()))
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(jwtUtil.getExpirationTime())
                    .tokenType("Bearer")
                    .build();

            log.info("User {} logged in successfully", user.getUsername());
            return new ApiResponse<>(200, ResMessages.SUCCESS, response);

        } catch (Exception e) {
            log.error("Login error: ", e);
            return new ApiResponse<>(409, ResMessages.SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<?> refreshToken(String refreshToken) {
        try {

            if (jwtUtil.isRefreshTokenExpired(refreshToken)) {
                return new ApiResponse<>(ResMessages.EXPIRED_TOKEN, 401);
            }

            String username = jwtUtil.getUsernameFromRefreshToken(refreshToken);
            Optional<AuthUser> userOptional = userRepository.findByUsername(username);

            if (userOptional.isEmpty()) {
                return new ApiResponse<>(ResMessages.USER_NOT_FOUND, 404);
            }

            AuthUser user = userOptional.get();

            if (!user.getIsActive()) {
                return new ApiResponse<>(ResMessages.USER_NOT_ACTIVE, 403);
            }

            String newAccessToken = jwtUtil.generateAccessToken(user.getUsername());

            LoginResponseDto response = LoginResponseDto.builder()
                    .user(UserResponseDto.toDto(user, new UserResponseDto()))
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(jwtUtil.getExpirationTime())
                    .tokenType("Bearer")
                    .build();

            return new ApiResponse<>(200, ResMessages.SUCCESS, response);

        } catch (Exception e) {
            log.error("Token refresh error: ", e);
            return new ApiResponse<>(ResMessages.EXPIRED_TOKEN, 409);
        }
    }

    @Override
    public ApiResponse<?> getMe() {
        try{
            Optional<String> currentUser = SessionUser.getCurrentUser();
            if (currentUser.isEmpty()) {
                return new ApiResponse<>(404, ResMessages.USER_NOT_FOUND);
            }

            String username = currentUser.get();
            Optional<AuthUser> byUsername = userRepository.findByUsername(username);
            if (byUsername.isEmpty()) {
                return new ApiResponse<>(404, ResMessages.USER_NOT_FOUND);
            }

            AuthUser authUser = byUsername.get();
            UserResponseDto response = UserResponseDto.toDto(authUser, new UserResponseDto());
            return new ApiResponse<>(200, ResMessages.SUCCESS, response);
        }catch (Exception e){
            log.error("get me error: ", e);
            return new ApiResponse<>(ResMessages.SERVER_ERROR, 409);
        }
    }
}
