package com.project.spring.digitalwallet.web.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.spring.digitalwallet.model.ErrorResponse;
import com.project.spring.digitalwallet.web.advice.ErrorHandlerControllerAdvice;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filters requests.
 */
@Component
public class FilterChainExceptionHandlerFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ErrorHandlerControllerAdvice controllerAdvice;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException | AuthenticationException e) {
            log.error("Spring Security Filter Chain Exception:", e);
            ResponseEntity<ErrorResponse> responseEntity =
                controllerAdvice.handleAuthenticationException(e);
            response.setStatus(responseEntity.getStatusCodeValue());
            PrintWriter out = response.getWriter();
            new ObjectMapper().writeValue(out, responseEntity.getBody());
        }
    }
}

