package com.viaas.certification.api.provider;

import com.viaas.certification.api.entity.FormAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * to verify userinfo from userDetailService
 */
@Component
public class FormAuthenticationProvider implements AuthenticationProvider {
    private UserDetailsService userDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        FormAuthenticationToken formAuthenticationToken = (FormAuthenticationToken) authentication;
        String userName = (String) authentication.getPrincipal();

        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        if(ObjectUtils.isEmpty(userDetails)){
            //TODO throw Exception
        }

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
