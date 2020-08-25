package org.bonn.se2.gui.components;

import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.MenuBar;
import org.bonn.se2.gui.view.ProfileView;
import org.bonn.se2.process.control.LoginControl;
import org.bonn.se2.process.control.ProfileControl;
import org.bonn.se2.services.util.Config;
import org.bonn.se2.services.util.SessionFunctions;
import org.bonn.se2.services.util.UIFunctions;



public class NavigationBar extends HorizontalLayout {

    public NavigationBar() {

        Image logo = createImage();

        this.addComponent(logo);
        this.setComponentAlignment(logo, Alignment.MIDDLE_LEFT);

        MenuBar menuBar = new MenuBar();

        MenuBar.Command userProfile = clickEvent -> {
            ProfileView.setMyProfile(true);
            ProfileControl.customerProfile();
        };

        MenuBar.Command companyProfile = clickEvent -> {
            ProfileView.setMyProfile(true);
            ProfileControl.salesmanProfile();
        };

        if (SessionFunctions.getCurrentRole().equals(Config.Roles.CUSTOMER)) {
            MenuBar.MenuItem profile = menuBar.addItem("Mein Profil verwalten", userProfile);
            profile.setIcon(VaadinIcons.USER);
        } else {
            MenuBar.MenuItem profile = menuBar.addItem("Mein Profil verwalten", companyProfile);
            profile.setIcon(VaadinIcons.USER);
        }

        MenuBar.MenuItem logout = menuBar.addItem("Logout", clickEvent -> LoginControl.logoutUser());
        logout.setIcon(VaadinIcons.SIGN_OUT);

        this.addComponent(menuBar);
        this.setComponentAlignment(menuBar, Alignment.MIDDLE_RIGHT);
    }


    private Image createImage() {
        ThemeResource themeResource = new ThemeResource("images/logo_hd_3.png");
        Image logo = new Image(null, themeResource);
        logo.setWidth("230px");
        logo.addStyleName("logo");
        logo.addClickListener((MouseEvents.ClickListener) event ->
                UIFunctions.gotoMain()
        );
        return logo;
    }

}
