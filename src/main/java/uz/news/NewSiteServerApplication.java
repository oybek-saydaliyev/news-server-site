package uz.news;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.news.entity.AuthUser;
import uz.news.entity.enums.AuthRole;
import uz.news.repository.UserRepository;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class NewSiteServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewSiteServerApplication.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0)
                userRepository.save(new AuthUser("Admin", passwordEncoder.encode("Admin123!"), AuthRole.ADMIN));
        };
    }
}
