package org.bonn.se2.gui.windows;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import org.bonn.se2.model.dao.CarDAO;
import org.bonn.se2.model.dao.SalesmanDAO;
import org.bonn.se2.model.objects.dto.Car;
import org.bonn.se2.model.objects.dto.Salesman;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.services.util.SessionFunctions;

import java.sql.SQLException;

public class CarCreationWindow extends Window {

    public CarCreationWindow(){
        this.setUp();
    }

    private void setUp(){
        Label title = new Label("Füllen Sie bitte alle Felder aus.");
        VerticalLayout creationLayout = new VerticalLayout();
        Button Speichern = new Button("Speichern");
        TextField brand = new TextField("Marke");
        TextField buildYear = new TextField("Baujahr");
        TextArea description = new TextArea("Beschreibung");
        TextField color = new TextField("Farbe");
        TextField price = new TextField("Preis");
        TextField model = new TextField("Modell");

        creationLayout.addComponents(title,brand,buildYear,description,color,price,model,Speichern);
        this.setContent(creationLayout);
        this.setWidth("17%");
        this.center();

        Speichern.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String Marke = brand.getValue();
                Integer Baujahr = Integer.parseInt(buildYear.getValue());
                String Beschreibung = description.getValue();
                String Farbe = color.getValue();
                String Preis = price.getValue();
                String Modell = model.getValue();
                Integer userID = SessionFunctions.getCurrentUser().getUserID();
                Integer salesmanID = null;
                try {
                    SalesmanDAO salesmanDAO = new SalesmanDAO();
                    Salesman salesman = salesmanDAO.retrieve(userID);
                    salesmanID = salesman.getSalesmanID();
                } catch (DatabaseException e) {
                    e.printStackTrace();
                }

                Car dto = new Car(Baujahr,Preis,Marke,Beschreibung,Farbe,Modell,salesmanID);
                try {
                    CarDAO carDao = new CarDAO();
                    carDao.create(dto);
                    close();
                    Notification notification = new Notification("Sie haben erfolgreich ein Auto erstellt.", Notification.Type.ASSISTIVE_NOTIFICATION);
                    notification.setPosition(Position.BOTTOM_CENTER);
                    notification.setDelayMsec(4000);
                    notification.show(Page.getCurrent());
                } catch (DatabaseException | SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
