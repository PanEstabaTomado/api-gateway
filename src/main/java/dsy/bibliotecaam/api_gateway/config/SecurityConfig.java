package dsy.bibliotecaam.api_gateway.config;

import dsy.bibliotecaam.api_gateway.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }
    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(auth -> auth
                        .pathMatchers(
                                /*Este es de Auth, el original*/
                                "/api/bibliotecaam/auth/**",

                                /*Dependencias del Gateway*/
                                "/swagger-ui",
                                "/swagger-ui/**",
                                "/swagger-ui/index.html",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/webjars/**",

                                /*Este es de Libro*/
                                "/api/bibliotecaam/libro/swagger-ui/**",
                                "/api/bibliotecaam/libro/v3/api-docs",
                                "/api/bibliotecaam/libro/v3/api-docs/**",

                                /*Este es de Donacion*/
                                "/api/bibliotecaam/donacion/swagger-ui/**",
                                "/api/bibliotecaam/donacion/v3/api-docs",
                                "/api/bibliotecaam/donacion/v3/api-docs/**",

                                /*Este es de Resenia*/
                                "/api/bibliotecaam/resenia/swagger-ui/**",
                                "/api/bibliotecaam/resenia/v3/api-docs",
                                "/api/bibliotecaam/resenia/v3/api-docs/**",

                                /*Este es de Asistencia*/
                                "/api/bibliotecaam/asistencia/swagger-ui/**",
                                "/api/bibliotecaam/asistencia/v3/api-docs",
                                "/api/bibliotecaam/asistencia/v3/api-docs/**",

                                /*Este es de Taller*/
                                "/api/bibliotecaam/taller/swagger-ui/**",
                                "/api/bibliotecaam/taller/v3/api-docs",
                                "/api/bibliotecaam/taller/v3/api-docs/**",

                                /*Este es de Usuario*/
                                "/api/bibliotecaam/usuario/swagger-ui/**",
                                "/api/bibliotecaam/usuario/v3/api-docs",
                                "/api/bibliotecaam/usuario/v3/api-docs/**",

                                /*Este es de Compra*/
                                "/api/bibliotecaam/compra/swagger-ui/**",
                                "/api/bibliotecaam/compra/v3/api-docs",
                                "/api/bibliotecaam/compra/v3/api-docs/**",

                                /*Este es de Prestamo*/
                                "/api/bibliotecaam/prestamo/swagger-ui/**",
                                "/api/bibliotecaam/prestamo/v3/api-docs",
                                "/api/bibliotecaam/prestamo/v3/api-docs/**",

                                /*Este es de Empleado*/
                                "/api/bibliotecaam/empleado/swagger-ui/**",
                                "/api/bibliotecaam/empleado/v3/api-docs",
                                "/api/bibliotecaam/empleado/v3/api-docs/**",

                                /*Este es de Sancion*/
                                "/api/bibliotecaam/sancion/swagger-ui/**",
                                "/api/bibliotecaam/sancion/v3/api-docs",
                                "/api/bibliotecaam/sancion/v3/api-docs/**",

                                /*Este es de Seguridad, el auth*/
                                "/api/bibliotecaam/auth/swagger-ui/**",
                                "/api/bibliotecaam/auth/v3/api-docs",
                                "/api/bibliotecaam/auth/v3/api-docs/**"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthFilter,
                        SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);
        return source;
    }

}
