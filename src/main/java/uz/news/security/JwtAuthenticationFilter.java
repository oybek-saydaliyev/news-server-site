package uz.news.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.news.dto.ErrorResponseDto;
import uz.news.exception.JwtTokenExpiredException;
import uz.news.exception.JwtTokenInvalidException;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            String username = jwtUtil.getUsernameFromAccessToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.isValidToken(token, username)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.debug("User '{}' authenticated successfully", username);
                } else {
                    log.warn("Token validation failed for user: {}", username);
                }
            }

        } catch (JwtTokenExpiredException e) {
            log.error("Token expired: {}", e.getMessage());
            sendErrorResponse(response, e.getMessage(), 401, request.getRequestURI());
            return;

        } catch (JwtTokenInvalidException e) {
            log.error("Invalid token: {}", e.getMessage());
            sendErrorResponse(response, e.getMessage(), 401, request.getRequestURI());
            return;

        } catch (UsernameNotFoundException e) {
            log.error("User not found: {}", e.getMessage());
            sendErrorResponse(response, "Foydalanuvchi topilmadi", 401, request.getRequestURI());
            return;

        } catch (Exception e) {
            log.error("Authentication error: ", e);
            sendErrorResponse(response, "Autentifikatsiya xatosi", 401, request.getRequestURI());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response,
                                   String message,
                                   int status,
                                   String path) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json; charset=UTF-8");

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .message(message)
                .code(status)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
