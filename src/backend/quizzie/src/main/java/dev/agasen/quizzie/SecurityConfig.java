package dev.agasen.quizzie;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
  
  @Bean
  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
    http
      .csrf(c -> c.disable())
      .authorizeExchange(c -> c.anyExchange().permitAll())
      .cors(cors -> cors.disable()
      // cors.configurationSource(req -> {
      //   var angularClient = new CorsConfiguration();
      //   angularClient.setAllowCredentials(false);
      //   angularClient.setAllowedOrigins(List.of("*"));
      //   angularClient.setAllowedMethods(List.of("*"));
      //   angularClient.setAllowedHeaders(List.of("*"));
      //   return angularClient;
      // })
      );

    return http.build();
  }
}
