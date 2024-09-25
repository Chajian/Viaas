package com.viaas.certification.api.entity;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class FormAuthenticationToken extends AbstractAuthenticationToken {
    private Object pricipal;
    private Object credentails;
    public FormAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object pricipal, Object credentails) {
        super(authorities);
        this.pricipal = pricipal;
        this.credentails = credentails;
        this.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
