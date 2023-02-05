package com.example.starter.base;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Optional;

import io.quarkus.security.identity.SecurityIdentity;

@ApplicationScoped
public class AuthenticatedUser implements Serializable {

    private final SecurityIdentity securityIdentity;
    @Inject
    public AuthenticatedUser(SecurityIdentity securityIdentity) {
        this.securityIdentity = securityIdentity;
    }

    public Optional<Object> get() {
        return Optional.empty();
    }

    public void logout() {

    }

}
