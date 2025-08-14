package com.example.AgriConnect.Configuration;

import com.example.AgriConnect.Service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

        @Autowired
        private MyUserDetailsService myUserDetailsService;
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                        .csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(requests -> requests
                                .requestMatchers(
                                        "/api/Agriconnect.com",
                                        "/api/Agriconnect",
                                        "/api/get-api-key",
                                        "/api/ChatBot",
                                        "/api/register",
                                        "/api/sucessfullyregister",
                                        "/api/terms",
                                        "/api/login",
                                        "/static/**",
                                        "/images/**",
                                        "/css/**",
                                        "/js/**",
                                        "/webjars/**",
                                        "/error",
                                        "/favicon.ico") // Explicitly permit favicon
                                .permitAll()
                                .anyRequest().authenticated())
                        .formLogin(form -> form
                                .loginPage("/api/login")
                                .defaultSuccessUrl("/api/Option", true)
                                .failureUrl("/api/login?error=true")
                                .permitAll())
                        .logout(logout -> logout
                                .logoutUrl("/api/logout")
                                .logoutSuccessUrl("/api/logout-success")
                                .invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll())
                        .exceptionHandling(exception -> exception
                                .accessDeniedPage("/api/unauthorized"))
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .invalidSessionUrl("/api/login")
                                .maximumSessions(1)
                                .expiredUrl("/api/login?expired=true"))
                        .build();
        }
        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setPasswordEncoder(passwordEncoder());
                provider.setUserDetailsService(myUserDetailsService);
                return provider;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(12);
        }

        @Configuration
        public static class WebConfig implements WebMvcConfigurer {
                @Override
                public void addResourceHandlers(ResourceHandlerRegistry registry) {
                        registry.addResourceHandler("/static/**")
                                .addResourceLocations("classpath:/static/")
                                .setCachePeriod(3600);

                        registry.addResourceHandler("/images/**")
                                .addResourceLocations("classpath:/static/images/")
                                .setCachePeriod(3600);

                        registry.addResourceHandler("/css/**")
                                .addResourceLocations("classpath:/static/css/")
                                .setCachePeriod(3600);

                        registry.addResourceHandler("/js/**")
                                .addResourceLocations("classpath:/static/js/")
                                .setCachePeriod(3600);
                }

                @Override
                public void addInterceptors(InterceptorRegistry registry) {
                        registry.addInterceptor(localeChangeInterceptor());
                }
        }

        @Bean
        public LocaleResolver localeResolver() {
                SessionLocaleResolver resolver = new SessionLocaleResolver();
                resolver.setDefaultLocale(new Locale("en")); // Changed to English as default, change to "hi" if preferred
                return resolver;
        }

        @Bean
        public static LocaleChangeInterceptor localeChangeInterceptor() {
                LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
                interceptor.setParamName("lang");
                return interceptor;
        }

        @Bean
        public MessageSource messageSource() {
                ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
                messageSource.setBasenames("messages_en", "messages_hi"); // No classpath prefix, just basenames
                messageSource.setDefaultEncoding("UTF-8"); // Ensure UTF-8
                messageSource.setFallbackToSystemLocale(false); // Avoid system locale fallback
                messageSource.setAlwaysUseMessageFormat(true); // Proper formatting
                return messageSource;
        }
}