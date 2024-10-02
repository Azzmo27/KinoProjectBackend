package com.example.kinobackend.model;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    MOVIE_OPERATOR,
    CUSTOMER,
    TICKET_INSPECTOR;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
