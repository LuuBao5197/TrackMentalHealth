package fpt.aptech.trackmentalhealth.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CORSConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // đường dẫn API
                        .allowedOrigins("http://localhost:5173") // cho phép React truy cập
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // phương thức cho phép
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}

