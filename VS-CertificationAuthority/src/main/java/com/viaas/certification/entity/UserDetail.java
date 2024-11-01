package com.viaas.certification.entity;

import com.viaas.certification.exception.CertificationCode;
import com.viaas.certification.exception.CertificationException;
import io.micrometer.common.util.StringUtils;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

public class UserDetail extends User {
    private Long userId;

    public UserDetail(@NonNull UserDTO userDTO){
        super(userDTO.getAccount(),userDTO.getPwd(),true, true, true, true, Collections.emptyList());
    }

    public UserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public UserDetail(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

}
