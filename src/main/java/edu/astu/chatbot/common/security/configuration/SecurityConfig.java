package edu.astu.chatbot.common.security.configuration;

import edu.astu.chatbot.common.security.filter.JwtAuthenticationFilter;
import edu.astu.chatbot.common.security.filter.JwtAuthorizationFilter;
import edu.astu.chatbot.common.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)  // Disable CSRF (typically used for stateless APIs)
                .cors()  // Enable CORS (Cross-Origin Resource Sharing)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Stateless sessions (use JWT)
                .and().authorizeHttpRequests(auth -> {
                    // Allow POST requests to /api/v1/users without authentication
                    auth.requestMatchers("/api/v1/users/register").permitAll();

                    // For any other request, require authentication
                    auth.anyRequest().authenticated();
                }).addFilter(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration)))  // Custom JWT authentication filter
                .addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)  // Custom JWT authorization filter
                .build();
    }
/*
* @Bean

            .cors()  // Enable CORS if needed
                auth.anyRequest().permitAll();
            })
            .build();
}

* */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}