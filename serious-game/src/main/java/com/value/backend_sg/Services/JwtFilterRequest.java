package com.value.backend_sg.Services;

import com.value.backend_sg.Utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtFilterRequest extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilterRequest.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws
            ServletException, IOException {
        String authorizationHaeder = httpServletRequest.getHeader("Authorization");
        log.info("validating token {}", authorizationHaeder);
        String username = null;
        String jwtToken = null;

        if (authorizationHaeder !=null && authorizationHaeder.startsWith("Bearer ")) {
            jwtToken = authorizationHaeder.substring(7);
            username = jwtUtils.extractUsername(jwtToken);
        }

        if (username!=null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails currentUserDetails = userService.loadUserByUsername(username);
            Boolean tokenValidated = jwtUtils.validateToken(jwtToken, currentUserDetails);
            if (tokenValidated) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(currentUserDetails,null,currentUserDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

}
