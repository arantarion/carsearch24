package org.bonn.se2.gui.ui;


import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import org.bonn.se2.gui.view.LoginView;
import org.bonn.se2.gui.view.MainView;
import org.bonn.se2.gui.view.UserPageView;
import org.bonn.se2.gui.view.RegisterView;
import org.bonn.se2.model.objects.dto.User;
import org.bonn.se2.services.util.Config;

import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        Navigator navi = new Navigator(this, this);

        navi.addView(Config.Views.MAIN, MainView.class);
        navi.addView(Config.Views.LOGIN, LoginView.class);
        navi.addView(Config.Views.REGISTRATION, RegisterView.class);
        navi.addView(Config.Views.USERPAGE, UserPageView.class);

        UI.getCurrent().getNavigator().navigateTo(Config.Views.LOGIN);
    }

    public MyUI getMyUI() {
        return (MyUI) UI.getCurrent();
    }

    @WebServlet(urlPatterns = "/*", name = "CarSearch24", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
