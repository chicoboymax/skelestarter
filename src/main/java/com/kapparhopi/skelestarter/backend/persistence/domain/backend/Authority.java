package com.kapparhopi.skelestarter.backend.persistence.domain.backend;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author mdrouin
 * @since 2018-04-03
 */
public class Authority implements GrantedAuthority {

    private final String authority;

    public Authority(String authority) {

        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
