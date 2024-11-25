package com.poly.shoptrangsuc.Config;

import com.poly.shoptrangsuc.Model.Role;
import com.poly.shoptrangsuc.Service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AccountService accountService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors() //  phép CORS
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/**","/api/user/news/list/**").permitAll()

                        .requestMatchers(
                                "/api/admin/orders/**","/api/admin/orders/search/**","/api/admin/order-details/**"
                                ,"/api/admin/order-status/**","/api/admin/orders/filter/**"
                                ,"/api/admin/order-details/**","/api/admin/news/**"
                                ,"/api/admin/product-details/**")
                                            .hasAnyAuthority(Role.ADMIN.name())


                        .requestMatchers(
                                "/api/staff/orders/**","/api/staff/orders/search/**","/api/staff/order-details/**"
                                ,"/api/staff/order-status/**","/api/staff/orders/filter/**"
                                ,"/api/staff/order-details/**","/api/staff/product-details/**")
                                        .hasAnyAuthority(Role.STAFF.name())

                        .requestMatchers
                                ("/api/user/**", "/api/user/products/**"
                                ,"/api/user/order-history/**","/api/user/comments/**"
                                ,"/api/user/contact/**")
                                            .hasAnyAuthority(Role.USER.name())


                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(accountService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}