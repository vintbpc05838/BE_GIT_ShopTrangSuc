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
public class SecurityConfiguration  {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AccountService accountService;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors() // Thêm dòng này để cho phép CORS
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers("/api/auth/**")
                        .permitAll()
                        .requestMatchers("/api/admin","/api/admin/accounts",
                                "/api/admin/accounts/manager/**",
                                "/api/admin/accounts/staff/**",
                                "/api/admin/accounts/customer/**",
                                "/api/admin/accounts/managers",
                                "/api/admin/accounts/staffs",
                                "/api/admin/admin-information",
                                "/api/admin/save/admin-information",
                                "/api/admin/change-password").hasAnyAuthority(Role.ADMIN.name())

                        .requestMatchers("/api/staff",
                                "/api/staff/staff-information",
                                "/api/staff/save/staff-information",
                                "/api/staff/staff-change-password").hasAnyAuthority(Role.STAFF.name())


                        .requestMatchers("/api/user",
                                "/api/user/user-information",
                                "/api/user/save/user-information",
                                "/api/user/address/add",
                                "/api/user/address/list",
                                "/api/user/address/update/{id}",
                                "/api/user/address/delete/{id}",
                                "/api/user/address/setDefault/{addressId}").hasAnyAuthority(Role.USER.name())

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
