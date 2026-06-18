package dsy.bibliotecaam.api_gateway.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
/*
@Component
public class JwtAuthFilter  extends OncePerRequestFilter {
    // Must be the exact same value as jwt.secret used by the "security" service
    // to sign tokens. Comes from config-server's shared config/application.yml.
    @Value("${jwt.secret}")
    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String token = getTokenFromRequest(request);

        // No token at all: just continue. If the route requires auth,
        // the SecurityFilterChain's authorizeHttpRequests will reject it.
        // If the route is permitAll (e.g. /auth/**, swagger), it goes through.
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = parseClaims(token);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                List<GrantedAuthority> authorities = (role != null)
                        ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                        : Collections.emptyList();

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException | JwtException ex) {
            // Token was present but invalid/expired: fail fast with a clear 401
            // instead of silently falling through to a generic 403 later.
            sendUnauthorized(response, "Token invalido o expirado");
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
*/