package com.example.starter.base;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;

import com.example.starter.base.security.AuthenticationContext;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@PermitAll
public class MainView extends VerticalLayout {

    @Inject
    GreetService greetService;

    @Inject
    AuthenticationContext authenticationContext;

    public MainView() {
        // Use TextField for standard text input
        TextField textField = new TextField("Your name");
        textField.addThemeName("bordered");

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Say hello", e -> Notification
                .show(greetService.greet(textField.getValue())));

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in
        // shared-styles.css.
        addClassName("centered-content");

        add(textField, button);

        add(new RouterLink("Protected", ProtectedView.class));
        add(new Button("Logout", ev -> authenticationContext.logout()));
    }

    @PostConstruct
    void post() {
        authenticationContext.getPrincipalName()
                .ifPresent(name -> add(new Div(new Text("User: " + name))));
    }
}
