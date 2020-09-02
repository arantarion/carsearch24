package org.bonn.se2.gui.components;

import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.bonn.se2.process.control.LoginControl;
import org.bonn.se2.services.util.Config;
import org.bonn.se2.services.util.SessionFunctions;
import org.bonn.se2.services.util.UIFunctions;

/**
 * @author Henry Weckermann
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class NavigationBar extends HorizontalLayout {

    public NavigationBar() {

        Image logo = createImage();

        this.addComponent(logo);
        this.setComponentAlignment(logo, Alignment.MIDDLE_LEFT);

        MenuBar menuBar = new MenuBar();

        HorizontalLayout labelLayout = new HorizontalLayout();
        Label textLabel = new Label("<h3>&emsp;&emsp; Willkommen auf CarSearch24! Finden Sie noch heute ein neues Auto &emsp;&emsp;&emsp;&emsp;</h3>", ContentMode.HTML);
        labelLayout.addComponent(textLabel);

        labelLayout.addLayoutClickListener(e -> {
            UIFunctions.gotoMain();
        });

        labelLayout.setStyleName("clickLabel");

        this.addComponent(labelLayout);
        this.setComponentAlignment(labelLayout, Alignment.MIDDLE_CENTER);

        String userFirstName = SessionFunctions.getCurrentUser().getFirstName();

        if (SessionFunctions.getCurrentRole().equals(Config.Roles.SALESMAN)) {
            MenuBar.MenuItem userSiteButton = menuBar.addItem(userFirstName + "'s Autos", clickEvent -> UIFunctions.gotoUserPage());
            userSiteButton.setIcon(VaadinIcons.USER);
        } else {
            MenuBar.MenuItem userSiteButton = menuBar.addItem(userFirstName + "'s Reservierungen", clickEvent -> UIFunctions.gotoUserPage());
            userSiteButton.setIcon(VaadinIcons.USER);
        }


        MenuBar.MenuItem logout = menuBar.addItem("Logout", clickEvent -> LoginControl.logoutUser());
        logout.setIcon(VaadinIcons.SIGN_OUT);

        this.addComponent(menuBar);
        this.setComponentAlignment(menuBar, Alignment.MIDDLE_RIGHT);
    }


    private Image createImage() {
        ThemeResource themeResource = new ThemeResource(Config.ImagePaths.CORPORATE);
        Image logo = new Image(null, themeResource);
        logo.setWidth("230px");
        logo.addStyleName("logo");
        logo.addClickListener((MouseEvents.ClickListener) event ->
                UIFunctions.gotoMain()
        );
        return logo;
    }

}
