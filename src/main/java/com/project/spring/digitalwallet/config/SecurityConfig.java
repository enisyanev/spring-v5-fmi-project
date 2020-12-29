package com.project.spring.digitalwallet.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import com.project.spring.digitalwallet.model.user.Role;
import com.project.spring.digitalwallet.service.UserService;
import com.project.spring.digitalwallet.web.jwt.FilterChainExceptionHandlerFilter;
import com.project.spring.digitalwallet.web.jwt.JwtAuthenticationEntryPoint;
import com.project.spring.digitalwallet.web.jwt.JwtAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private FilterChainExceptionHandlerFilter filterChainExceptionHandlerFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers(POST,"/api/login", "/api/register").permitAll()
                .antMatchers(GET,"/api/users/**").hasAnyRole(Role.ADMIN.toString())
                .antMatchers(POST, "/api/send-money").hasAnyRole(Role.ADMIN.toString(), Role.USER.toString())
                //.antMatchers(POST, "/api/wallets").hasAnyRole(Role.ADMIN.toString(),Role.USER.toString())
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(filterChainExceptionHandlerFilter, LogoutFilter.class);
    }

    @Bean
    public UserDetailsService getUserDetailsService(UserService userService) {
        return userService::getUserByUsername;
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
