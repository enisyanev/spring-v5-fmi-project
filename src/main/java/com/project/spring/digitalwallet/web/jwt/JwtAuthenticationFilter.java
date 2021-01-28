package com.project.spring.digitalwallet.web.jwt;

import com.project.spring.digitalwallet.model.user.User;
import com.project.spring.digitalwallet.service.UserService;
import com.project.spring.digitalwallet.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter for JWT authentication.
 */
@Component
@Slf4j
@Order
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
            "Authorization,Content-Type, Content-Range, Content-Disposition, Content-Description,"
                + "Origin, X-Requested-With, sessionId");
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        if (authorizationHeader != null) {
            if (authorizationHeader.startsWith("Bearer ")) {
                jwtToken = authorizationHeader.substring(7);
                try {
                    username = jwtUtils.getUsernameFromToken(jwtToken);
                } catch (IllegalArgumentException ex) {
                    log.error("Unable to get JWT token.");
                    throw new BadCredentialsException("Unable to get JWT token.");
                } catch (ExpiredJwtException ex) {
                    log.error("JWT token has expired.");
                    throw new BadCredentialsException("JWT token has expired.");
                }
            } else {
                log.error("JWT token does not begin with 'Bearer ' prefix.");
                throw new BadCredentialsException("WT token does not begin with 'Bearer ' prefix.");
            }
        }
        if (username != null) {
            User user = userService.getUserByUsername(username);
            System.out.println(user.getAuthorities());
            if (jwtUtils.validateToken(jwtToken, user)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                System.out.println(user.getAuthorities());
                authenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
