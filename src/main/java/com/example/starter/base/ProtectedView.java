package com.example.starter.base;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("protected")
@RolesAllowed("admin")
public class ProtectedView extends Div {

    public ProtectedView() {
        add(new Text("a protected view"));
    }
}
