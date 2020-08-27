package org.bonn.se2.gui.view;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.EmailValidator;
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
import org.bonn.se2.model.dao.CustomerDAO;
import org.bonn.se2.model.dao.SalesmanDAO;
import org.bonn.se2.model.objects.dto.Customer;
import org.bonn.se2.model.objects.dto.Salesman;
import org.bonn.se2.model.objects.dto.User;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.services.util.CustomEmailValidator;
import org.bonn.se2.services.util.PasswordValidator;
import org.bonn.se2.services.util.SessionFunctions;
import org.bonn.se2.services.util.UIFunctions;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterView extends VerticalLayout implements View {

    private final Panel auswahlPanel = new Panel("Schritt 1: Registrieren als");
    private final Panel userCreationPanel = new Panel("Schritt 2: Geben Sie Ihre Daten ein");
    private final Binder<User> userBinder = new Binder<>();
    private final Binder<Customer> customerBinder = new Binder<>();
    private final Binder<Salesman> salesmanBinder = new Binder<>();
    boolean isCustomer;

    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (SessionFunctions.isLoggedIn()) {
            UIFunctions.gotoMain();
        } else {
            createHeader();
            this.setUpStep1();
        }
    }

    public void setUpStep1() {

        auswahlPanel.setVisible(true);

        this.addComponent(auswahlPanel);

        this.setComponentAlignment(auswahlPanel, Alignment.MIDDLE_CENTER);

        auswahlPanel.setWidth(37, Unit.PERCENTAGE);
        auswahlPanel.setHeight(20, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        HorizontalLayout prevButtonLayout = new HorizontalLayout();
        content.addComponent(buttonLayout);
        content.addComponent(prevButtonLayout);
        content.setComponentAlignment(prevButtonLayout, Alignment.MIDDLE_CENTER);

        auswahlPanel.setContent(content);
        buttonLayout.setSizeFull();

        Button customerButton = new Button("Kunde");
        Button salesmanButton = new Button("Vertriebler");
        Button backButton = new Button("Zurück", VaadinIcons.ARROW_CIRCLE_LEFT);

        prevButtonLayout.addComponent(backButton);
        prevButtonLayout.setComponentAlignment(backButton, Alignment.MIDDLE_CENTER);

        buttonLayout.setSpacing(false);
        buttonLayout.addComponents(customerButton, salesmanButton);

        customerButton.setWidth(100, Unit.PERCENTAGE);
        salesmanButton.setWidth(100, Unit.PERCENTAGE);
        backButton.setWidth(30, Unit.PERCENTAGE);

        customerButton.setHeight("100px");
        salesmanButton.setHeight("100px");
        backButton.setHeight("40px");

        buttonLayout.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);

        customerButton.addClickListener(event -> {
            auswahlPanel.setVisible(false);
            this.isCustomer = true;
            setUpStep2();
        });

        salesmanButton.addClickListener(event -> {
            auswahlPanel.setVisible(false);
            this.isCustomer = false;
            setUpStep2();
        });

        backButton.addClickListener(event ->
                UIFunctions.gotoLogin()
        );

    }

    public void setUpStep2() {

        userCreationPanel.setVisible(true);
        this.addComponent(userCreationPanel);
        this.setComponentAlignment(userCreationPanel, Alignment.MIDDLE_CENTER);
        userCreationPanel.setWidth("550px");

        Button weiterButton1 = new Button("Fortfahren", VaadinIcons.ARROW_RIGHT);
        weiterButton1.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        Button backButton = new Button("Zurück", VaadinIcons.ARROW_LEFT);

        FormLayout content = new FormLayout();

        TextField firstNameField = new TextField("Vorname:");
        firstNameField.setRequiredIndicatorVisible(true);
        userBinder.forField(firstNameField).asRequired("Bitte geben Sie Ihren Vornamen an")
                .bind(User::getFirstName, User::setFirstName);
        firstNameField.setSizeFull();

        TextField lastNameField = new TextField("Nachname:");
        lastNameField.setRequiredIndicatorVisible(true);
        userBinder.forField(lastNameField).asRequired("Bitte geben Sie Ihren Nachname an")
                .bind(User::getLastName, User::setLastName);
        lastNameField.setSizeFull();

        TextField emailField = new TextField("E-Mail Adresse");
        if (isCustomer) {
            userBinder.forField(emailField).asRequired(new EmailValidator("Bitte geben Sie eine gültige E-Mail Adresse an"))
                    .bind(User::getEmail, User::setEmail);
        } else {
            userBinder.forField(emailField).asRequired(new CustomEmailValidator("Bitte geben Sie eine gültige E-Mail Adresse an"))
                    .bind(User::getEmail, User::setEmail);
        }
        emailField.setSizeFull();

        PasswordField passwordField = new PasswordField("Passwort");
        userBinder.forField(passwordField).asRequired(new PasswordValidator())
                .bind(User::getPassword, User::setPassword);
        passwordField.setSizeFull();

        PasswordField passwordCheckField = new PasswordField("Passwort wiederholen");
        passwordCheckField.setSizeFull();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponents(backButton, weiterButton1);

        content.addComponents(firstNameField, lastNameField, emailField, passwordField, passwordCheckField, buttonLayout);

        content.setMargin(true);
        userCreationPanel.setContent(content);

        this.setComponentAlignment(userCreationPanel, Alignment.MIDDLE_CENTER);

        backButton.addClickListener(clickEvent -> {
            userCreationPanel.setVisible(false);

            setUpStep1();
        });

        weiterButton1.addClickListener(clickEvent -> {

            boolean isValidEntry = true;

            if (!passwordField.getValue().equals(passwordCheckField.getValue())) {
                Notification notification = new Notification("Die Passwörter stimmen nicht überein.", Notification.Type.ERROR_MESSAGE);
                notification.setPosition(Position.BOTTOM_CENTER);
                notification.setDelayMsec(4000);
                notification.show(Page.getCurrent());
                return;
            }

            User myUser = new User();
            try {
                userBinder.writeBean(myUser);
            } catch (ValidationException exception) {
                showError();
                return;
            }

            userCreationPanel.setVisible(false);

            if (isCustomer) {
                Customer customer = new Customer(myUser);

                try {
                    customerBinder.writeBean(customer);
                    CustomerDAO customerDao = new CustomerDAO();
                    customerDao.create(customer);
                    setUpStep4();

                } catch (ValidationException | DatabaseException | SQLException e1) {
                    isValidEntry = false;
                    Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                            new Throwable().getStackTrace()[0].getMethodName() + " failed", e1);
                }

            } else {
                Salesman salesman = new Salesman(myUser);

                try {
                    salesmanBinder.writeBean(salesman);
                    SalesmanDAO salesmanDao = new SalesmanDAO();
                    salesmanDao.create(salesman);
                    setUpStep4();

                } catch (ValidationException | DatabaseException | SQLException e1) {
                    isValidEntry = false;
                    Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                            new Throwable().getStackTrace()[0].getMethodName() + " failed", e1);
                }
            }

            if (!isValidEntry) {
                showError();
            }

        });

    }


    private void setUpStep4() {
        Notification notification = new Notification("Sie haben sich erfolgreich registriert", Notification.Type.ASSISTIVE_NOTIFICATION);
        notification.setPosition(Position.MIDDLE_CENTER);
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
        notification.setStyleName("mystyle");
        UIFunctions.gotoLogin();
    }


    public void createHeader() {
        ThemeResource themeResource = new ThemeResource("images/corporate.png"); //TODO
        Image logo = new Image(null, themeResource);
        logo.setWidth("750px");
        logo.addStyleName("logo");
        logo.addClickListener((MouseEvents.ClickListener) event ->
                UIFunctions.gotoLogin()
        );

        Label platzhalterLabel = new Label("&nbsp", ContentMode.HTML);

        Label labelText = new Label("Willkommen bei CarSearch24! Finden Sie noch heute ein neues Auto.");

        this.addComponent(logo);
        this.addComponent(labelText);

        this.addComponent(platzhalterLabel);
        platzhalterLabel.setHeight("40px");

        this.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(labelText, Alignment.MIDDLE_CENTER);

    }

    public void showError() {
        Notification notification = new Notification("Ein oder mehrere Felder sind ungültig", Notification.Type.ERROR_MESSAGE);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(4000);
        notification.show(Page.getCurrent());
    }

}
