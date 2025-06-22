package fpt.aptech.trackmentalhealth.configs;

import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.filter.JwtAuthenticationFilter;
import fpt.aptech.trackmentalhealth.repository.login.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)

public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private LoginRepository loginRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            System.out.println("Tìm user theo email: " + email);
            Users users = loginRepository.findByEmail(email);
            if (users == null) {
                System.out.println("Không tìm thấy user với email: " + email);
                throw new UsernameNotFoundException("User not found");
            }

//            if (Boolean.FALSE.equals(users.getIsApproved())) {
//                throw new UsernameNotFoundException("Account not approved by admin");
//            }

            return User.builder()
                    .username(users.getEmail())
                    .password(users.getPassword())
                    .roles(users.getRoleId().getRoleName().toUpperCase())
                    .build();
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/users/login",
                                "/api/users/register",
                                "/api/users/forgot-password",
                                "/api/users/verify-otp",
                                "/api/users/reset-password",
                                "/api/users/pending-registrations",
                                "/api/users/approve/**"
                        ).permitAll()

                        // Chỉ ADMIN mới được xem user theo role
                        .requestMatchers("/api/users/by-role/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/profile").hasRole("ADMIN")

                        .requestMatchers("/index").hasRole("ADMIN")
                        .requestMatchers("/user").hasRole("USER")
                        .requestMatchers("/psychologist").hasRole("PSYCHOLOGIST")
                        .requestMatchers("/content_creator").hasRole("CONTENT_CREATOR")
                        .requestMatchers("/test_designer").hasRole("TEST_DESIGNER")

                        .requestMatchers("/api/users/edit-profile").authenticated()

                        .anyRequest().authenticated()
                );
        // Thêm JWT filter trước filter xác thực mặc định
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173"); // 👈 frontend origin
        configuration.addAllowedMethod("*");                     // GET, POST, etc.
        configuration.addAllowedHeader("*");                     // All headers
        configuration.setAllowCredentials(true);                 // For cookies/token

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // apply globally
        return source;
    }


}