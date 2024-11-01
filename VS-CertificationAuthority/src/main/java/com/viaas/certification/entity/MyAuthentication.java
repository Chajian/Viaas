package com.viaas.certification.entity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

import java.util.Collection;
import java.util.Map;

public class MyAuthentication extends AbstractOAuth2TokenAuthenticationToken {

    public MyAuthentication(OAuth2Token token) {
        super(token);
    }

    protected MyAuthentication(OAuth2Token token, Collection collection) {
        super(token, collection);
    }

    protected MyAuthentication(OAuth2Token token, Object principal, Object credentials, Collection collection) {
        super(token, principal, credentials, collection);
    }

    @Override
    public Map<String, Object> getTokenAttributes() {
        return null;
    }
}
