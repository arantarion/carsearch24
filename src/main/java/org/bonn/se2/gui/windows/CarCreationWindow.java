package org.bonn.se2.gui.windows;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import org.bonn.se2.model.dao.CarDAO;
import org.bonn.se2.model.dao.SalesmanDAO;
import org.bonn.se2.model.objects.dto.Car;
import org.bonn.se2.model.objects.dto.Salesman;
import org.bonn.se2.model.objects.dto.User;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.services.util.SessionFunctions;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarCreationWindow extends Window {

    private final Binder<Car> carBinder = new Binder<>();

    public CarCreationWindow() {
        this.setUp();
    }

    private void setUp() {
        VerticalLayout creationLayout = new VerticalLayout();

        Label title = new Label("Füllen Sie bitte alle Felder aus.");

        TextField brand = new TextField("Marke");
        TextField buildYear = new TextField("Baujahr");
        TextArea description = new TextArea("Beschreibung");
        TextField color = new TextField("Farbe");
        TextField price = new TextField("Preis");
        TextField model = new TextField("Modell");

        Button saveButton = new Button("Speichern");

        brand.setSizeFull();
        model.setSizeFull();
        buildYear.setSizeFull();
        description.setSizeFull();
        color.setSizeFull();
        price.setSizeFull();
        saveButton.setSizeFull();

        creationLayout.addComponents(title, brand, model, buildYear, color, description, price, saveButton);

        this.setContent(creationLayout);
        this.setWidth("17%");
        this.center();

        carBinder.forField(buildYear)
                .withConverter(new StringToIntegerConverter("Bitte geben Sie ein gültiges Jahr an!"))
                .withValidator(year -> year >= 1900 && year < 2100, "Bitte geben Sie ein gültiges Jahr an!")
                .bind(Car::getBuildYear, Car::setBuildYear);


        saveButton.addClickListener((Button.ClickListener) clickEvent -> {
            String marke = brand.getValue();
            String beschreibung = description.getValue();
            String farbe = color.getValue();
            String preis = price.getValue();
            String modell = model.getValue();
            Integer userID = SessionFunctions.getCurrentUser().getUserID();
            Integer salesmanID = null;
            Integer baujahr = null;

            try {
                baujahr = Integer.parseInt(buildYear.getValue());
            } catch (NumberFormatException nve) {
                Notification notification = new Notification("Kein gültiges Jahr.", Notification.Type.ERROR_MESSAGE);
                notification.setPosition(Position.BOTTOM_CENTER);
                notification.setDelayMsec(4000);
                notification.show(Page.getCurrent());
                return;
            }

            try {
                SalesmanDAO salesmanDAO = new SalesmanDAO();
                Salesman salesman = salesmanDAO.retrieve(userID);
                salesmanID = salesman.getSalesmanID();
            } catch (DatabaseException e) {
                Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                        new Throwable().getStackTrace()[0].getMethodName() + " failed", e);
            }

            Car dto = new Car(baujahr, preis, marke, beschreibung, farbe, modell, salesmanID);
            try {
                CarDAO carDao = new CarDAO();
                carDao.create(dto);
                close();
                Notification notification = new Notification("Sie haben erfolgreich ein Auto erstellt.", Notification.Type.ASSISTIVE_NOTIFICATION);
                notification.setPosition(Position.BOTTOM_CENTER);
                notification.setDelayMsec(4000);
                notification.show(Page.getCurrent());
            } catch (DatabaseException | SQLException e) {
                Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                        new Throwable().getStackTrace()[0].getMethodName() + " failed", e);
            }
        });
    }
}
