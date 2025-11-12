package is.hi.hbv501gteam23.Config;

import is.hi.hbv501gteam23.Security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                        "/auth/login",
                        "/auth/register"
                    ).permitAll()
                    .requestMatchers(HttpMethod.GET,
                        "/players/**",
                        "/venues/**",
                        "/matches/**",
                        "/teams/**",
                        "/favorites/**",
                        "/metadata/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                ).permitAll()
                .requestMatchers("/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,
                        "/players/**",
                        "/venues/**",
                        "/matches/**",
                        "/teams/**"
                ).hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH,
                        "/players/**",
                        "/venues/**",
                        "/matches/**",
                        "/teams/**"
                ).hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,
                        "/players/**",
                        "/venues/**",
                        "/matches/**",
                        "/teams/**"
                ).hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid or missing token");
                })
                .accessDeniedHandler((req, res, e) -> {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden: You do not have permission");
                })
        );
        return http.build();
    }
}
