package com.example.starter.base;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.auth.ViewAccessChecker;

public class ViewAccessCheckerInitializer implements VaadinServiceInitListener {

    private final ViewAccessChecker viewAccessChecker;

    @Inject
    public ViewAccessCheckerInitializer(ViewAccessChecker viewAccessChecker) {
        //viewAccessChecker = new ViewAccessChecker();
        //viewAccessChecker.setLoginView(LoginView.class);
        this.viewAccessChecker = viewAccessChecker;
    }

    @Override
    public void serviceInit(@Observes ServiceInitEvent serviceInitEvent) {
        serviceInitEvent.getSource().addUIInitListener(uiInitEvent -> {
            uiInitEvent.getUI().addBeforeEnterListener(viewAccessChecker);
        });
    }
}