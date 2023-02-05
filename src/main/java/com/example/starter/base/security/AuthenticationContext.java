package com.example.starter.base.security;

import javax.inject.Singleton;
import java.security.Principal;
import java.util.Optional;

import io.quarkus.logging.Log;
import io.quarkus.security.identity.SecurityIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The authentication context of the application.
 * <p>
 *
 * It allows to access authenticated user information and to initiate the logout
 * process.
 *
 * An instance of this class is available for injection as bean in view and
 * layout classes. The class is not {@link java.io.Serializable}, so potential
 * referencing fields in Vaadin views should be defined {@literal transient}.
 *
 * @author Vaadin Ltd
 * @since 23.3
 */
@Singleton
public class AuthenticationContext {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AuthenticationContext.class);

    private final SecurityIdentity securityIdentity;
    private final LogoutHandler logoutHandler;

    public AuthenticationContext(SecurityIdentity securityIdentity,
            LogoutHandler logoutHandler) {
        this.securityIdentity = securityIdentity;
        this.logoutHandler = logoutHandler;
    }

    /**
     * Gets an {@link Optional} with an instance of the current user if it has
     * been authenticated, or empty if the user is not authenticated.
     *
     * Anonymous users are considered not authenticated.
     *
     * @param <U>
     *            the type parameter of the expected user instance
     * @param userType
     *            the type of the expected user instance
     * @return an {@link Optional} with the current authenticated user, or empty
     *         if none available
     * @throws ClassCastException
     *             if the current user instance does not match the given
     *             {@code userType}.
     */
    public <U> Optional<U> getAuthenticatedUser(Class<U> userType) {
        return Optional.ofNullable(securityIdentity.getPrincipal())
                .map(userType::cast);
    }

    /**
     * Gets an {@link Optional} containing the authenticated principal name, or
     * an empty optional if the user is not authenticated.
     *
     * The principal name usually refers to a username or an identifier that can
     * be used to retrieve additional information for the authenticated user.
     *
     * Anonymous users are considered not authenticated.
     *
     * @return an {@link Optional} containing the authenticated principal name
     *         or an empty optional if not available.
     */
    public Optional<String> getPrincipalName() {
        return Optional.ofNullable(securityIdentity.getPrincipal())
                .map(Principal::getName);
    }

    /**
     * Indicates whether a user is currently authenticated.
     *
     * Anonymous users are considered not authenticated.
     *
     * @return {@literal true} if a user is currently authenticated, otherwise
     *         {@literal false}
     */
    public boolean isAuthenticated() {
        return !securityIdentity.isAnonymous();
    }

    /**
     * Initiates the logout process of the current authenticated user by
     * invalidating the local session and then notifying
     * {@link org.springframework.security.web.authentication.logout.LogoutHandler}.
     */
    public void logout() {
        logoutHandler.logout();
    }

}
