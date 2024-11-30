package mx.edu.utez.sigede_backend.security;

import mx.edu.utez.sigede_backend.security.config.TokenJwtConfig;
import mx.edu.utez.sigede_backend.security.filters.JwtAuthenticationFilter;
import mx.edu.utez.sigede_backend.security.filters.JwtValidationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class SpringSecurityConfig {

    private final  AuthenticationConfiguration authenticationConfiguration;
    private final TokenJwtConfig tokenJwtConfig;

    public SpringSecurityConfig(AuthenticationConfiguration authenticationConfiguration, TokenJwtConfig tokenJwtConfig) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.tokenJwtConfig = tokenJwtConfig;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager() throws  Exception{
        return  authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers(HttpMethod.GET,"/api/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/**").permitAll()
                        //.requestMatchers(HttpMethod.POST,"/api/*").hasAnyRole("SUPERADMIN","ADMIN","CAPTURIST")
                        .requestMatchers(HttpMethod.PUT,"/api/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/*").hasAnyRole("SUPERADMIN","ADMIN","CAPTURIST")
                        .anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), tokenJwtConfig))
                .addFilter(new JwtValidationFilter(authenticationManager(), tokenJwtConfig))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
        config.setAllowedHeaders(List.of("Authorization","Content-Type"));
        config.setExposedHeaders(List.of("Content-Disposition"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);
        return  source;

    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter(){
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;

    }
}
