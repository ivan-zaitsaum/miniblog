package com.example.miniblog.config;

import com.example.miniblog.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserService userService;
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    // 1) Определяем, как подгружать пользователя из нашей UserService
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userService
                .findByUsername(username)
                .map(user -> User.withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles("USER")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    // 2) Провайдер аутентификации (DAO)
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService());
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    // 3) PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 4) Основной SecurityFilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authProvider())
                .authorizeHttpRequests(auth -> auth
                        // разрешаем доступ всем к этим путям
                        .requestMatchers("/css/**", "/login", "/register").permitAll()
                        // всё остальное – только аутентифицированным
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // указываем свой контроллер и шаблон
                        .loginPage("/login")
                        // URL, куда сабмитится форма логина
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/posts", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
        // отключаем CSRF только если у тебя REST; для формы оставь включённым
        //.csrf(csrf -> csrf.disable())
        ;
        return http.build();
    }
}
