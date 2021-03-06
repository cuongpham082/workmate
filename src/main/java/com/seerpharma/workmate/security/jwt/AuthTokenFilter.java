package com.seerpharma.workmate.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.seerpharma.workmate.security.service.impl.UserDetailsServiceImpl;
import com.seerpharma.workmate.util.Constants;


public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            if (!isUriValid(request)) {
                if (!checkAuthorization(request)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                String jwt = parseJwt(request);
                if (jwt != null && jwtUtils.getClaimsJwsViaJwtToken(jwt) != null) {
                    String username = jwtUtils.getClaimsJwsViaJwtToken(jwt).getBody().getSubject();
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private Boolean checkAuthorization(HttpServletRequest request) {
        return request.getHeader(Constants.HEADER_AUTHORIZATION) != null;
    }

    private Boolean isUriValid(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.equals(Constants.LOGIN_URI)
                || requestURI.equals(Constants.TOKEN_URI)
                || requestURI.equals(Constants.ACTIVE_USER_URI)
                || requestURI.contains(Constants.FORGOT_PASSWORD_URI);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(Constants.HEADER_AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(Constants.ACCESS_TOKEN_TYPE)) {
            return headerAuth.substring(Constants.ACCESS_TOKEN_TYPE.length() + 1, headerAuth.length());
        }
        return null;
    }

}
