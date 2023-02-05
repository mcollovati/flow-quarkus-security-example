package com.example.starter.base.security;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.stream.Stream;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;

@Singleton
public class VaadinLogoutHandler implements LogoutHandler {

    @ConfigProperty(name = "vaadin.quarkus-security.logout-url")
    String logoutUrl;

    @ConfigProperty(name = "vaadin.quarkus-security.invalidate-session", defaultValue = "false")
    boolean invalidateSession;

    @ConfigProperty(name = "vaadin.quarkus-security.cookie-name")
    String cookieName;

    @Override
    public void logout() {
        HttpServletRequest request = VaadinServletRequest.getCurrent()
                .getHttpServletRequest();
        HttpServletResponse response = VaadinServletResponse.getCurrent()
                .getHttpServletResponse();
        if (request != null) {
            try {
                request.logout();
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalStateException("Cannot logout");
        }
        Stream.of(request.getCookies())
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .forEach(cookie -> {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                });
        HttpSession session = request.getSession(false);
        if (invalidateSession && session != null) {
            session.invalidate();
        }
        final UI ui = UI.getCurrent();
        ui.accessSynchronously(() -> {
            sendRedirect(request, response, logoutUrl);
        });
    }

    private void sendRedirect(HttpServletRequest request,
            HttpServletResponse response, String url) {
        final var servletMapping = request.getHttpServletMapping().getPattern();
        if (HandlerHelper.isFrameworkInternalRequest(servletMapping, request)) {
            UI ui = UI.getCurrent();
            if (ui != null) {
                ui.getPage().setLocation(url);
            } else {
                LoggerFactory.getLogger(VaadinLogoutHandler.class).warn(
                        "A redirect to {} was request during a Vaadin request, "
                                + "but it was not possible to get the UI instance to perform the action.",
                        url);
            }
        } else {
            try {
                response.sendRedirect(url);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
    }

}
