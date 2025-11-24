package is.hi.hbv501gteam23.Config;

import is.hi.hbv501gteam23.Security.AuditCleanupFilter;
import is.hi.hbv501gteam23.Security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final AuditCleanupFilter auditCleanupFilter;

    public SecurityConfig(AuditCleanupFilter auditCleanupFilter) {
        this.auditCleanupFilter = auditCleanupFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(16, 32, 1, 65536, 4);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/login",
                    "/auth/signup"
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
                .requestMatchers("/error").permitAll()
                .requestMatchers("/users/**").hasRole("ADMIN")
                .requestMatchers("/profile/**").authenticated()
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
            .headers(headers -> headers
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000)
                )
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives(
                        "default-src 'self'; " +
                        "img-src 'self' data:; " +
                        "script-src 'self'; " +
                        "style-src 'self';" +
                        "connect-src 'self';"
                    )
                )
                .referrerPolicy(ref -> ref
                    .policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER)
                )
                .addHeaderWriter((request, response) -> {
                    response.setHeader(
                        "Permissions-Policy",
                        "geolocation=(), microphone=(), payment=(), usb=()"
                    );
                })
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(auditCleanupFilter, JwtAuthenticationFilter.class);

            return http.build();
    }
}
