package fpt.aptech.trackmentalhealth.filter;

import fpt.aptech.trackmentalhealth.ultis.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");
        String userEmail = null;
        String jwtToken = null;

        if (header != null && header.startsWith("Bearer ")) {
            jwtToken = header.substring(7);
            userEmail = jwtUtils.extractUsername(jwtToken);
            System.out.println("Extracted userEmail from JWT: " + userEmail);
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtils.validateToken(jwtToken, userEmail)) {
                // 🔥 Lấy roles từ JWT
                Claims claims = jwtUtils.extractAllClaims(jwtToken);
                List<String> roles = claims.get("roles", List.class); // ví dụ: ["ROLE_ADMIN"]

                // 🔥 Chuyển thành list authority
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList(); // nếu Java <16, dùng Collectors.toList()

                var userDetails = userDetailsService.loadUserByUsername(userEmail); // 👈 bắt buộc để Spring công nhận
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("✅ Authenticated as: " + userDetails.getUsername());
                System.out.println("Authorities: " + authentication.getAuthorities());
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.equals("/api/users/login")
                || path.equals("/api/users/register")
                || path.equals("/api/users/forgot-password")
                || path.equals("/api/users/verify-otp")
                || path.equals("/api/users/reset-password")
                || path.startsWith("/api/users/approve/**") // 👈 bỏ qua xác thực cho approve
                || path.startsWith("/api/users/pending-registrations") // 👈 bỏ qua xác thực cho pending list
                || path.startsWith("/api/chat")
                || path.startsWith("/api/appointment")
                || path.startsWith("/moods");
    }

}