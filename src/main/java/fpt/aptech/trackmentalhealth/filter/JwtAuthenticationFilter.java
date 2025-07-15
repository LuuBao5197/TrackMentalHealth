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

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtils jwtUtil;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");
        String userEmail = null;
        String jwtToken = null;

        if (header != null && header.startsWith("Bearer ")) {
            jwtToken = header.substring(7);
            userEmail = jwtUtil.extractUsername(jwtToken);
            System.out.println("Extracted userEmail from JWT: " + userEmail);
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwtToken, userEmail)) {
                // Load user details để lấy roles
                var userDetails = userDetailsService.loadUserByUsername(userEmail);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
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
                || path.equals("/api/users/verify-otp-register")
                || path.startsWith("/api/users/approve")
                || path.startsWith("/api/users/pending-registrations")
                || path.startsWith("/api/chat")
                || path.startsWith("/api/appointment")
                || path.startsWith("/moods")
                || path.startsWith("/api/chat/**")
                || path.startsWith("/api/notification/**")
                || path.startsWith("/api/users/profile/**")
                || path.equals("/api/users/reset-password");

    }

}