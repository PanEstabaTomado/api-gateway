package dsy.bibliotecaam.api_gateway.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthFilter implements WebFilter {

    @Value("${jwt.secret}")
    private String secret;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/bibliotecaam/auth",
            "/swagger-ui",
            "/v3/api-docs",
            "/webjars"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        boolean isPublic = PUBLIC_PATHS.stream()
                .anyMatch(path::startsWith)
                || path.contains("v3/api-docs")
                || path.contains("/swagger-ui");

        if (isPublic) {
            return chain.filter(exchange);
        }
        System.out.println(">>> path: "+path+" isPublic: "+isPublic);
        String token = getToken(exchange.getRequest());

        if (token == null) {
            return chain.filter(exchange);
        }

        try {
            Map<String, Object> claims = verifyAndDecode(token);

            Object expClaim = claims.get("exp");
            if (expClaim instanceof Number expSeconds
                    && new Date(((Number) expSeconds).longValue() * 1000)
                    .before(new Date())) {
                return unauthorized(exchange, "Token expirado");
            }

            String username = (String) claims.get("sub");
            String role = (String) claims.get("role");

            List<GrantedAuthority> authorities = (role != null)
                    ? Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + role))
                    : Collections.emptyList();

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            username, null, authorities);

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder
                            .withAuthentication(auth));

        } catch (Exception ex) {
            return unauthorized(exchange, "Token invalido");
        }
    }

    private Map<String, Object> verifyAndDecode(String token)
            throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new Exception("Malformado");

        String datos = parts[0] + "." + parts[1];
        byte[] claveBytes = Base64.getDecoder().decode(secret);
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(claveBytes, "HmacSHA256"));
        byte[] firmaEsperada = mac.doFinal(
                datos.getBytes(StandardCharsets.UTF_8));
        byte[] firmaReal = Base64.getUrlDecoder().decode(parts[2]);

        if (!MessageDigest.isEqual(firmaEsperada, firmaReal)) {
            throw new Exception("Firma invalida");
        }

        byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
        return objectMapper.readValue(payloadBytes, Map.class);
    }

    private String getToken(ServerHttpRequest request) {
        String header = request.getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange,
                                    String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);
        byte[] body = ("{\"error\": \"" + message + "\"}").getBytes();
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse()
                        .bufferFactory().wrap(body)));
    }
}
