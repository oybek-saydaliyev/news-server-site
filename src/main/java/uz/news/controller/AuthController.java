package uz.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.news.base.ApiResponse;
import uz.news.dto.auth.LoginRequestDto;
import uz.news.service.AuthService;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto){
        return ApiResponse.controller(authService.login(dto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam String refreshToken){
        return ApiResponse.controller(authService.refreshToken(refreshToken));
    }

    @GetMapping("/getMe")
    public ResponseEntity<?> getMe(){
        return ApiResponse.controller(authService.getMe());
    }
}
