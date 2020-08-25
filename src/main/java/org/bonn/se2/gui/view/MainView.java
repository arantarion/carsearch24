package org.bonn.se2.gui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.bonn.se2.gui.components.NavigationBar;
import org.bonn.se2.services.util.Config;
import org.bonn.se2.services.util.SessionFunctions;



public class MainView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        if (!SessionFunctions.isLoggedIn()) {
            UI.getCurrent().getNavigator().navigateTo(Config.Views.LOGIN);
        } else {
            this.parentSetUp();
            this.setUp();
        }
    }

    public void parentSetUp() {
        NavigationBar navigationBar = new NavigationBar();
        this.addComponent(navigationBar);
        this.setComponentAlignment(navigationBar, Alignment.TOP_CENTER);
    }

    public void setUp() {

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        HorizontalLayout horizontalLayoutCompany = new HorizontalLayout();
        HorizontalLayout h2 = new HorizontalLayout();

        Button suche = new Button("Suchen", VaadinIcons.SEARCH);
        TextField name = new TextField();
        Label label = new Label("Bitte geben Sie ein Stichwort ein:");

        Label labelText = new Label("Willkommen auf CarSearch24! Finden Sie noch heute ein neues Auto.");

        addComponent(labelText);
        setComponentAlignment(labelText, Alignment.MIDDLE_CENTER);
        addComponent(horizontalLayout);
        setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);

        addComponent(h2);
        setComponentAlignment(h2, Alignment.TOP_LEFT);

        addComponent(horizontalLayoutCompany);
        setComponentAlignment(horizontalLayoutCompany, Alignment.MIDDLE_CENTER);
        horizontalLayoutCompany.addComponent(label);
        horizontalLayoutCompany.addComponent(name);
        horizontalLayoutCompany.addComponent(new Label("&nbsp", ContentMode.HTML)); // Label erstellt, um textfeld und Button zu trennen (Abstand größer ist)
        horizontalLayoutCompany.addComponent(suche);

    }

}
