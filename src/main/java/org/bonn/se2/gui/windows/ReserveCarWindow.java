package org.bonn.se2.gui.windows;

import com.vaadin.ui.*;
import org.bonn.se2.model.dao.CustomerDAO;
import org.bonn.se2.model.dao.ReservationDAO;
import org.bonn.se2.model.objects.dto.Car;
import org.bonn.se2.model.objects.dto.Customer;
import org.bonn.se2.model.objects.dto.Reservation;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.services.util.SessionFunctions;

public class ReserveCarWindow extends Window {

    private final Car car;
    private int carID;
    private int customerID;

    public ReserveCarWindow(Car car) throws DatabaseException {
        this.car = car;
        setUp();
    }

    private void setUp() throws DatabaseException {

        VerticalLayout reservationLayout = new VerticalLayout();

        Label title = new Label("Wollen Sie wirklich dieses Auto reservieren?");

        Label brand = new Label("Marke: " + car.getBrand());
        Label buildYear = new Label("Baujahr: " + car.getBuildYear());
        Label color = new Label("Farbe: " + car.getColor());
        Label price = new Label("Preis: " + car.getPrice());
        Label model = new Label("Modell: " + car.getModel());

        brand.setSizeFull();
        model.setSizeFull();
        buildYear.setSizeFull();
        color.setSizeFull();
        price.setSizeFull();

        reservationLayout.addComponents(title, brand, model, buildYear, color, price);


        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button yes = new Button("JA");
        Button no = new Button("NEIN");

        yes.setWidth("100%");
        no.setWidth("100%");
        yes.setHeight("75px");
        no.setHeight("75px");

        yes.addStyleName("friendly");
        no.addStyleName("danger");
        buttonLayout.addComponents(yes, no);

        buttonLayout.setComponentAlignment(yes, Alignment.MIDDLE_LEFT);
        buttonLayout.setComponentAlignment(no, Alignment.MIDDLE_RIGHT);

        buttonLayout.setWidth("100%");

        reservationLayout.addComponent(buttonLayout);

        this.setContent(reservationLayout);
        this.setWidth("22%");
        this.center();

        carID = car.getCarID();
        int userID = SessionFunctions.getCurrentUser().getUserID();
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.retrieve(userID);
        customerID = customer.getCustomerID();

        yes.addClickListener(e -> {
            try {
                ReservationDAO reservationDAO = new ReservationDAO();
                Reservation reservation = new Reservation(carID, customerID);
                reservationDAO.create(reservation);
                close();
            } catch (DatabaseException databaseException) {
                databaseException.printStackTrace();
            }
        });

        no.addClickListener(e -> {
            close();
        });
    }
}
