package com.example.AgriConnect.Configuration;

import com.example.AgriConnect.Service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private MyUserDetailsService MyuserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/Agriconnect.com", "/api/Agriconnect","/api/AgriconnectChatBot", "/api/register","static/**","/image/**", "/api/sucessfullyregister", "/api/terms", "/api/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/api/login")
                        .defaultSuccessUrl("/api/Option", true)
                        .failureUrl("/api/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                                .logoutUrl("/api/logout")
                                        .logoutSuccessUrl("/api/logout-success")  // Changed this URL
                                        .invalidateHttpSession(true)
                                        .clearAuthentication(true)
                                        .deleteCookies("JSESSIONID")
                                        .permitAll()
                                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/api/unauthorized")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));  // BCrypt with strength 12
        provider.setUserDetailsService(MyuserDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);  // Define a PasswordEncoder bean
    }
}
