package bt.edu.gcit.usermicroservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class BhutanBeyondSecurityConfig {

    public BhutanBeyondSecurityConfig() {
        System.out.println("BhutanBeyondSecurityConfig created");
    }

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList(authProvider()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // allow all your booking endpoints
                        .anyRequest().permitAll());

        // .addFilterBefore(jwtRequestFilter,
        // UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ✅ Global CORS config for React frontend
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://159.223.52.109")); // React frontend
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

// .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
// .requestMatchers(HttpMethod.POST, "/api/auth/logout").permitAll()
// .requestMatchers(HttpMethod.POST, "/api/tourists/find").permitAll()
// .requestMatchers(HttpMethod.POST, "/api/users/find").permitAll()
// .requestMatchers(HttpMethod.POST, "/api/roles").permitAll()
// .requestMatchers(HttpMethod.PUT, "/api/tourists/updateDetails").permitAll()
// .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
// .requestMatchers(HttpMethod.GET,
// "/api/users/checkDuplicateEmail").permitAll()
// .requestMatchers(HttpMethod.GET, "/api/users/guide/all").permitAll()
// .requestMatchers(HttpMethod.PUT, "/api/users/{id}").permitAll()
// .requestMatchers(HttpMethod.DELETE, "/api/users/delete/{id}").permitAll()
// .requestMatchers(HttpMethod.PUT, "/api/users/{id}/enabled").permitAll()
// .requestMatchers(HttpMethod.POST, "/api/users/{id}/disable").permitAll()
// .requestMatchers(HttpMethod.POST, "/api/tourists/*").permitAll()
// .requestMatchers(HttpMethod.POST, "/api/tourists/changePassword").permitAll()
// .requestMatchers(HttpMethod.PUT, "/api/tourists/{id}/enabled").permitAll()
// .requestMatchers(HttpMethod.DELETE, "/api/tourists/delete/{id}").permitAll()
// .requestMatchers(HttpMethod.GET, "/api/tourists/all").permitAll()
// .requestMatchers(HttpMethod.GET, "/api/tourists/{id}").permitAll()
// .requestMatchers(HttpMethod.POST, "/api/feedbacks").permitAll()
// .requestMatchers(HttpMethod.GET, "/api/feedbacks/all").permitAll()
// .requestMatchers(HttpMethod.GET, "/api/feedbacks/{id}").permitAll()
// .requestMatchers(HttpMethod.DELETE, "/api/feedbacks/delete/{id}").permitAll()
// .requestMatchers(HttpMethod.GET, "/api/tourists/{id}/resend-otp").permitAll()
// .requestMatchers(HttpMethod.POST, "/api/packages").permitAll()
// .requestMatchers(HttpMethod.GET, "/api/packages/all").permitAll()
// .requestMatchers(HttpMethod.GET, "/api/packages/{id}").permitAll()
// .requestMatchers(HttpMethod.PUT, "/api/packages/update/{id}").permitAll()
// .requestMatchers(HttpMethod.DELETE, "/api/packages/delete/{id}").permitAll()
// .anyRequest().authenticated())
