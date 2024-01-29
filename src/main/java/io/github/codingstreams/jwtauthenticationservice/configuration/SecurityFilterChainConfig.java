package io.github.codingstreams.jwtauthenticationservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityFilterChainConfig {
  private final AuthenticationEntryPoint authenticationEntryPoint;
  private final JWTAuthenticationFilter jwtAuthenticationFilter;

  public SecurityFilterChainConfig(AuthenticationEntryPoint authenticationEntryPoint, JWTAuthenticationFilter jwtAuthenticationFilter) {
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.cors(corsConfig->corsConfig.configurationSource(getConfigurationSource()));

    // Disable CSRF
    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    // Http Request Filter
    httpSecurity.authorizeHttpRequests(
        requestMatcher ->
            requestMatcher.requestMatchers("/api/auth/login/**").permitAll()
                .requestMatchers("/api/auth/sign-up/**").permitAll()
                .requestMatchers("/api/auth/verify-token/**").permitAll()
                .anyRequest().authenticated()
    );

    // Authentication Entry Point -> Exception Handler
    httpSecurity.exceptionHandling(
        exceptionConfig -> exceptionConfig.authenticationEntryPoint(authenticationEntryPoint)
    );

    // Set session policy = STATELESS
    httpSecurity.sessionManagement(
        sessionConfig->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    );

    // Add JWT Authentication Filter
    httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }

  private static CorsConfigurationSource getConfigurationSource(){
    var corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedMethods(List.of("*"));
    corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000/", "http://localhost:8080"));
    corsConfiguration.setAllowedHeaders(List.of("Content-Type"));

    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);

    return  source;
  }
}
