package org.bonn.se2.gui.view;

import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.bonn.se2.gui.ui.MyUI;
import org.bonn.se2.model.objects.dto.User;
import org.bonn.se2.model.objects.dto.UserAtLogin;
import org.bonn.se2.process.control.LoginControl;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.process.control.exceptions.InvalidCredentialsException;
import org.bonn.se2.services.util.Config;
import org.bonn.se2.services.util.UIFunctions;

/**
 * @author Henry Weckermann, Anton Drees
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class LoginView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        User user = ((MyUI) UI.getCurrent()).getUser();

        if (user != null) {
            UI.getCurrent().getNavigator().navigateTo(Config.Views.MAIN);
        }

        this.setUp();
    }

    private void setUp() {
        ThemeResource themeResource = new ThemeResource(Config.ImagePaths.CORPORATE);
        Image logo = new Image(null, themeResource);
        logo.setWidth("750px");
        logo.addStyleName("logo");
        logo.addClickListener((MouseEvents.ClickListener) event ->
                UIFunctions.gotoLogin()
        );

        Label platzhalterLabel = new Label("&nbsp", ContentMode.HTML);

        Label labelText = new Label("<h2>Willkommen bei CarSearch24! Finden Sie noch heute ein neues Auto.</h2>", ContentMode.HTML);

        this.addComponent(logo);
        this.addComponent(labelText);

        this.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(labelText, Alignment.MIDDLE_CENTER);
        this.addComponents(platzhalterLabel);
        platzhalterLabel.setHeight("60px");


        final TextField userLogin = new TextField();
        userLogin.setCaption("E-Mail:");
        userLogin.setPlaceholder("Geben Sie hier Ihre E-Mail an");
        userLogin.setSizeFull();

        final PasswordField passwd = new PasswordField();
        passwd.setCaption("Passwort:");
        passwd.setPlaceholder("Passwort eingeben");
        passwd.setSizeFull();

        VerticalLayout layout = new VerticalLayout();
        layout.addComponents(userLogin, passwd);

        Panel panel = new Panel("Bitte Login-Daten angeben:");

        this.addComponent(panel);
        this.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

        panel.setContent(layout);

        Button loginButton = new Button("Login", VaadinIcons.PAPERPLANE);
        layout.addComponent(loginButton);
        layout.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(userLogin, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(passwd, Alignment.MIDDLE_CENTER);
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        Button registrierungsButton = new Button("Registrierung", VaadinIcons.ARROW_CIRCLE_RIGHT_O);
        layout.addComponent(registrierungsButton);
        layout.setComponentAlignment(registrierungsButton, Alignment.MIDDLE_CENTER);

        panel.setSizeUndefined();
        panel.setWidth("350px");

        loginButton.addClickListener(e -> {
            String login = userLogin.getValue();
            String password = passwd.getValue();

            try {
                LoginControl.checkAuthentication(new UserAtLogin(login, password));
            } catch (InvalidCredentialsException ex) {
                Notification notification = new Notification("Kein User mit diesen Zugangsdaten gefunden!", Notification.Type.ERROR_MESSAGE);
                notification.setPosition(Position.BOTTOM_CENTER);
                notification.setDelayMsec(4000);
                notification.show(Page.getCurrent());
                passwd.setValue("");
            } catch (DatabaseException ex) {
                Notification.show("DB-Fehler", ex.getReason(), Notification.Type.ERROR_MESSAGE);
                userLogin.setValue("");
                passwd.setValue("");
            }

        });

        registrierungsButton.addClickListener(e -> UI.getCurrent().getNavigator().navigateTo(Config.Views.REGISTRATION));

    }

}
