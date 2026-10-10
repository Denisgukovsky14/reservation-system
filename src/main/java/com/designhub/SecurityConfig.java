package com.designhub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Открыто без токена: главная страница, вход/регистрация (иначе как попасть внутрь?),
    // 3D-рабочий стол (отдельная, ещё не подключённая к общей логике задача — не трогаем)
    // и вся статика (CSS/JS/картинки/страницы) — токен живёт в localStorage и едет только
    // с fetch()-запросами из JS, обычная навигация по ссылке его не несёт, так что дальше
    // защищаем API, а не сами .html-файлы.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/index.html", "/home",
                                "/3Dtable", "/3Dtable/**",
                                "/api/auth/**",
                                "/src/**", "/assets/**", "/models/**", "/uploads/**",
                                "/favicon.ico"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // без токена/с битым токеном → 401 (а не дефолтные 403), чтобы фронтенд мог
                // отличить "надо залогиниться" от осознанного 403 из бизнес-логики (см. ProfileController)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
