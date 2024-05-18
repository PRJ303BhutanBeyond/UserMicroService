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

import java.util.Arrays;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import bt.edu.gcit.usermicroservice.security.oauth.TouristOAuth2UserService;
import bt.edu.gcit.usermicroservice.security.oauth.OAuth2LoginSuccessHandler;

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

    @Autowired
    private TouristOAuth2UserService oAuth2UserService;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        System.out.println("H2i ");
        return new ProviderManager(Arrays.asList(authProvider()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        System.out.println("Hi ");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        System.out.println("UserDetailsService: " + userDetailsService.getClass().getName());
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        System.out.println("AuthProvider: " + authProvider.getClass().getName());

        return authProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer -> configurer
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/logout").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/tourists/find").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/tourists/updateDetails").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users/checkDuplicateEmail").hasAuthority("Admin")
                .requestMatchers(HttpMethod.GET, "/api/users/guide/all").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAuthority("Admin")
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}/enabled").hasAuthority("Admin")
                .requestMatchers(HttpMethod.POST, "/api/tourists/*").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/tourists/{id}/enabled").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/tourists/delete/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tourists/all").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tourists/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/feedbacks").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/feedbacks/all").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/feedbacks/{id}").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/feedbacks/delete/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tourists/{id}/resend-otp").permitAll()

        )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        http.oauth2Login()
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler);

        // disable CSRF
        http.csrf().disable();

        return http.build();
    }

}
