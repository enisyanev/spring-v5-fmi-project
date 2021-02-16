package com.project.spring.digitalwallet.config;

import com.project.spring.digitalwallet.service.UserService;
import com.project.spring.digitalwallet.utils.UserPermission;
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

    private static final String[] ALL_ROLES = UserPermission.getAllPermissions().stream().map(p -> p.toString())
            .toArray(String[]::new);

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
                .antMatchers(GET, "/api/users/management").hasAnyRole(UserPermission.CAN_USE_USER_MANAGEMENT.toString())
                .antMatchers(GET, "/api/users/**").hasAnyRole(ALL_ROLES)
                .antMatchers(DELETE, "/api/users/**").hasAnyRole(UserPermission.CAN_USE_USER_MANAGEMENT.toString())
                .antMatchers(POST, "/api/send-money/**")
                .hasAnyRole(UserPermission.CAN_SEND_MONEY.toString())
                .antMatchers(POST, "/api/group/**")
                .hasAnyRole(UserPermission.CAN_CREATE_GROUP.toString())
                .antMatchers(GET, "/api/transactions-history").hasAnyRole(UserPermission.CAN_SEE_TR_HISTORY.toString())
                .antMatchers(POST, "/api/send-money").hasAnyRole(UserPermission.CAN_SEND_MONEY.toString())
                .antMatchers(POST, "/api/upload").hasAnyRole(UserPermission.CAN_DEPOSIT.toString())
                .antMatchers(GET, "/api/recurring-payments").hasAnyRole(UserPermission.CAN_USE_RECCURINGS.toString())
                .antMatchers(POST, "/api/recurring-payments").hasAnyRole(UserPermission.CAN_USE_RECCURINGS.toString())
                .antMatchers(DELETE, "/api/recurring-payments/**").hasAnyRole(UserPermission.CAN_USE_RECCURINGS.toString())
                .antMatchers(PUT, "/api/recurring-payments/**").hasAnyRole(UserPermission.CAN_USE_RECCURINGS.toString())
                .antMatchers(POST, "/api/payment-instruments/**").hasAnyRole(UserPermission.CAN_DEPOSIT.toString())
                .antMatchers(PUT, "/api/payment-instruments/**").hasAnyRole(UserPermission.CAN_DEPOSIT.toString())
                .antMatchers(GET, "/api/permissions").hasAnyRole(UserPermission.CAN_USE_USER_MANAGEMENT.toString())
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
