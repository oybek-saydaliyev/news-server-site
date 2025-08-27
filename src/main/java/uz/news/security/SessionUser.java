package uz.news.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public final class SessionUser {

    private SessionUser(){}

    public static Optional<String> getCurrentUser(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            return Optional.of(userDetails.getUsername());
        } else if (authentication.getPrincipal() instanceof String) {
            return Optional.of((String) authentication.getPrincipal());
        }
        return Optional.empty();
    }
}
