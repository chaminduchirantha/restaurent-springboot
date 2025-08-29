package lk.ijse.gdse.restaurentspringbootbackend.Config;

import lk.ijse.gdse.restaurentspringbootbackend.repo.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig implements WebMvcConfigurer {
    private final CustomerRepo userRepo;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepo.findByUsername(username)
        .map(user -> new User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        )).orElseThrow(
                () -> new RuntimeException("User not found")
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("uploads");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/uploads/")
                .addResourceLocations("file:" + uploadPath+"/");
    }
}
