package gateway.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HardCorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String origin = request.getHeader("Origin");
        if (origin != null && !origin.isBlank()) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Vary", appendVary(response.getHeader("Vary"), "Origin"));
        }

        String reqHeaders = request.getHeader("Access-Control-Request-Headers");
        if (reqHeaders != null && !reqHeaders.isBlank()) {
            response.setHeader("Access-Control-Allow-Headers", reqHeaders);
            response.setHeader("Vary", appendVary(response.getHeader("Vary"), "Access-Control-Request-Headers"));
        } else {
            response.setHeader("Access-Control-Allow-Headers", "*");
        }

        String reqMethod = request.getHeader("Access-Control-Request-Method");
        if (reqMethod != null && !reqMethod.isBlank()) {
            response.setHeader("Access-Control-Allow-Methods", reqMethod);
            response.setHeader("Vary", appendVary(response.getHeader("Vary"), "Access-Control-Request-Method"));
        } else {
            response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        }

        response.setHeader("Access-Control-Max-Age", "3600");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String appendVary(String existing, String add) {
        if (existing == null || existing.isBlank()) return add;
        String[] parts = existing.split(",");
        for (String p : parts) {
            if (p.trim().equalsIgnoreCase(add)) return existing;
        }
        return existing + ", " + add;
    }
}
