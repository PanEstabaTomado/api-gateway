package dsy.bibliotecaam.api_gateway.config;

import dsy.bibliotecaam.api_gateway.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf ->
                        csrf.disable()).sessionManagement(
                                session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                        auth.requestMatchers(
                                "/api/bibliotecaam/auth/**",
                                /*Este es de libro*/
                                "/api/bibliotecaam/libro/swagger-ui/**",
                                "/api/bibliotecaam/libro/v3/api-docs/**",
                                /*Este es de donacion*/
                                "/api/bibliotecaam/donacion/swagger-ui/**",
                                "/api/bibliotecaam/donacion/v3/api-docs/**",
                                /*Este es de sancion*/
                                "/api/bibliotecaam/sancion/swagger-ui/**",
                                "/api/bibliotecaam/sancion/v3/api-docs/**",

                                /*Este es de empleado*/
                                "/api/bibliotecaam/empleado/swagger-ui/**",
                                "/api/bibliotecaam/empleado/v3/api-docs/**",

                                /*Este es de compra*/
                                "/api/bibliotecaam/compra/swagger-ui/**",
                                "/api/bibliotecaam/compra/v3/api-docs/**",

                                /*Este es de Taller*/
                                "/api/bibliotecaam/taller/swagger-ui/**",
                                "/api/bibliotecaam/taller/v3/api-docs/**",

                                /*Este es de Asistencia*/
                                "/api/bibliotecaam/asistencia/swagger-ui/**",
                                "/api/bibliotecaam/asistencia/v3/api-docs/**",

                                /*Este es de Prestamo*/
                                "/api/bibliotecaam/prestamo/swagger-ui/**",
                                "/api/bibliotecaam/prestamo/v3/api-docs/**",
                                /*Este es de reseña*/
                                "/api/bibliotecaam/resenia/swagger-ui/**",
                                "/api/bibliotecaam/resenia/v3/api-docs/**",
                                /*Este es de usuario*/
                                "/api/bibliotecaam/usuario/swagger-ui/**",
                                "/api/bibliotecaam/usuario/v3/api-docs/**",
                                /*Este es de seguridad*/
                                "/api/bibliotecaam/auth/swagger-ui/**",
                                "/api/bibliotecaam/auth/v3/api-docs/**",
                                /*Otros que dejo por si acaso necesito hacerlo mas flexible*/
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**"
                                        ).permitAll();
                        auth.anyRequest().authenticated();
                        })
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
