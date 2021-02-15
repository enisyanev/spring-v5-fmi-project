package com.project.spring.digitalwallet.config;


import com.project.spring.digitalwallet.model.user.Role;
import com.project.spring.digitalwallet.service.UserService;
import com.project.spring.digitalwallet.web.jwt.FilterChainExceptionHandlerFilter;
import com.project.spring.digitalwallet.web.jwt.JwtAuthenticationEntryPoint;
import com.project.spring.digitalwallet.web.jwt.JwtAuthenticationFilter;
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

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.DELETE;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] ALL_ROLES =
            new String[]{Role.ADMIN.toString(), Role.USER.toString()};

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
                .antMatchers(POST, "/api/login", "/api/register").permitAll()
                .antMatchers(GET, "/api/register/user").hasAnyRole(ALL_ROLES)
                .antMatchers(GET, "/api/users/**").hasAnyRole(ALL_ROLES)
                .antMatchers(DELETE, "/api/users/**").hasAnyRole(ALL_ROLES)
                .antMatchers(POST, "/api/send-money/**")
                .hasAnyRole(Role.ADMIN.toString(), Role.USER.toString())
                .antMatchers(POST, "/api/group/**")
                .hasAnyRole(Role.ADMIN.toString(), Role.USER.toString())
                .antMatchers(POST, "/api/transactions-history").hasAnyRole(ALL_ROLES)
                .antMatchers(GET, "/api/transactions-history").hasAnyRole(ALL_ROLES)
                .antMatchers(POST, "/api/send-money").hasAnyRole(ALL_ROLES)
                .antMatchers(POST, "/api/upload").hasAnyRole(ALL_ROLES)
                .antMatchers(GET, "/api/recurring-payments").hasAnyRole(ALL_ROLES)
                .antMatchers(POST, "/api/recurring-payments").hasAnyRole(ALL_ROLES)
                .antMatchers(DELETE, "/api/recurring-payments/**").hasAnyRole(ALL_ROLES)
                .antMatchers(PUT, "/api/recurring-payments/**").hasAnyRole(ALL_ROLES)
                .antMatchers(POST, "/api/payment-instruments/**").hasAnyRole(ALL_ROLES)
                .antMatchers(PUT, "/api/payment-instruments/**").hasAnyRole(ALL_ROLES)
                .antMatchers(GET, "/api/permissions").hasAnyRole(ALL_ROLES)
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
