package com.viaas.certification.api.filter;

import com.viaas.certification.api.entity.FormAuthenticationToken;
import com.viaas.certification.api.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * verify token by JwtUtil
 */
@Component
public class FormAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Value("${AUTH..METHOD}:POST")
    private static final String REQUEST_METHOD = "POST";
    @Value("${AUTH.LOGIN}:/ibs/api/verify/login")
    private static final String AUTH_PATH = "/ibs/api/verify/login";

    private AuthenticationProvider authenticationProvider;

    private JwtUtil jwtUtil;
    protected FormAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher, AuthenticationProvider authenticationProvider, JwtUtil jwtUtil) {
        super(new AntPathRequestMatcher(AUTH_PATH,REQUEST_METHOD));
        this.authenticationProvider = authenticationProvider;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //unexpect parameter handler
        if(!REQUEST_METHOD.equals(request.getMethod())){

        }
        else{
            final String authorizationHeader = request.getHeader("Authorization");

            String username = null;
            String jwt = null;

            // Check if Authorization header contains a Bearer token
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);  // Remove "Bearer " prefix to get the actual token
                try {
                    username = jwtUtil.extractUsername(jwt);  // Extract username from the JWT
                } catch (ExpiredJwtException e) {
                    System.out.println("JWT token has expired");
                } catch (Exception e) {
                    System.out.println("Error parsing JWT token");
                }
            }

            // If the token is valid, authenticate the user
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                if (jwtUtil.validateToken(jwt, username)) {
                    // If token is valid, manually set the authentication in the security context
                    FormAuthenticationToken authenticationToken = new FormAuthenticationToken(null,username,null);

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    return authenticationProvider.authenticate(authenticationToken);
                }
            }
        }
        //TODO throw exception
        return null;
    }
}
