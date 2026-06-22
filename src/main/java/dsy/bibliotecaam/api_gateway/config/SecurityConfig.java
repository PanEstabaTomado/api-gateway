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

                                        /*Este es de sancion*/

                                        /*Este es de empleado*/

                                        /*Este es de compra*/

                                        /*Este es de Taller*/

                                        /*Este es de Asistencia*/

                                        /*Este es de Prestamo*/

                                        /*Este es de reseña*/

                                        /*Este es de usuario*/

                                        /*Este es de seguridad*/
                                        "/api/bibliotecaam/security/swagger-ui/**",
                                        "/api/bibliotecaam/security/v3/api-docs/**",
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
