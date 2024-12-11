package com.back.websocket.config;

import com.back.websocket.user.service.CustomAuthenticationFailureHandler;
import com.back.websocket.user.service.CustomAuthenticationSuccessHandler;
import com.back.websocket.user.service.CustomUserDetailsService;
import com.back.websocket.user.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,ApplicationContext applicationContext) throws Exception {


        http.csrf(AbstractHttpConfigurer::disable);

        http
                .authorizeHttpRequests((auth) -> auth
                        //.requestMatchers("/afeedback/**").hasRole("ADMIN")
                        .requestMatchers("/sign-up").permitAll()
                        .anyRequest().authenticated());

        http
                .formLogin((formLogin) ->
                formLogin
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login")
                        .successHandler(authenticationSuccessHandler()) // 로그인 성공 핸들러
                        .failureHandler(authenticationFailureHandler(applicationContext)) // 로그인 실패 핸들러
                        .permitAll()
        );

        http.addFilterBefore(new CustomRequestLoggingFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(ApplicationContext applicationContext) {
        return new CustomAuthenticationFailureHandler(applicationContext);
    }
}
