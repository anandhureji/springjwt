package com.anandhu.springjwt.filter;

import com.anandhu.springjwt.service.JwtService;
import com.anandhu.springjwt.service.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /** OncePerRequestFilter used to execute this Filter in every incoming request... **/

    private final JwtService jwtService;

    private final UserDetailsImpl userDetailService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsImpl userDetails) {
        this.jwtService = jwtService;
        this.userDetailService = userDetails;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        String token = authHeader.substring(7);
        String userName = jwtService.extractUsername(token);
        if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null){

            UserDetails userDetails = userDetailService.loadUserByUsername(userName);
            if(jwtService.isValid(token,userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);

    }



}
