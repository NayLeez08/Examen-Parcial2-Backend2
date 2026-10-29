
package com.examen.backend2.config;

import com.examen.backend2.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.equals("/auth/registro")
                || path.equals("/auth/login");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null
                || !authorization.startsWith("Bearer ")
                || authorization.substring(7).trim().isEmpty()) {
            rechazar(response);
            return;
        }

        String token = authorization.substring(7).trim();
        String username;

        try {
            if (!jwtService.esTokenValido(token)) {
                rechazar(response);
                return;
            }

            username = jwtService.extraerUsername(token);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            rechazar(response);
            return;
        }

        var usuario = User.withUsername(username)
                .password("")
                .authorities(Collections.emptyList())
                .build();

        var authentication = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                Collections.emptyList()
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource()
                        .buildDetails(request)
        );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private void rechazar(HttpServletResponse response)
            throws IOException {

        SecurityContextHolder.clearContext();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                "{\"error\":\"Se requiere un token JWT válido\"}"
        );
    }
}
