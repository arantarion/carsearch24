package org.bonn.se2.gui.components;

import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.bonn.se2.process.control.LoginControl;
import org.bonn.se2.services.util.UIFunctions;


public class NavigationBar extends HorizontalLayout {

    public NavigationBar() {

        Image logo = createImage();

        this.addComponent(logo);
        this.setComponentAlignment(logo, Alignment.MIDDLE_LEFT);

        MenuBar menuBar = new MenuBar();

        Label textLabel = new Label("<h3>&emsp;&emsp; Willkommen auf CarSearch24! Finden Sie noch heute ein neues Auto &emsp;&emsp;&emsp;&emsp;</h3>", ContentMode.HTML);
        this.addComponent(textLabel);
        this.setComponentAlignment(textLabel, Alignment.MIDDLE_CENTER);

        MenuBar.MenuItem logout = menuBar.addItem("Logout", clickEvent -> LoginControl.logoutUser());
        logout.setIcon(VaadinIcons.SIGN_OUT);

        this.addComponent(menuBar);
        this.setComponentAlignment(menuBar, Alignment.MIDDLE_RIGHT);
    }


    private Image createImage() {
        ThemeResource themeResource = new ThemeResource("images/corporate.png");
        Image logo = new Image(null, themeResource);
        logo.setWidth("230px");
        logo.addStyleName("logo");
        logo.addClickListener((MouseEvents.ClickListener) event ->
                UIFunctions.gotoMain()
        );
        return logo;
    }

}
