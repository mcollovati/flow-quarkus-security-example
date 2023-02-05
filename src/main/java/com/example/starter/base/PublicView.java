package com.example.starter.base;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("public")
@AnonymousAllowed
public class PublicView extends Div {

    public PublicView() {
        add(new Text("a public view"));
    }
}
