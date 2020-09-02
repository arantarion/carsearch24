package org.bonn.se2.gui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.bonn.se2.gui.components.NavigationBar;
import org.bonn.se2.model.dao.*;
import org.bonn.se2.model.objects.dto.Car;
import org.bonn.se2.model.objects.dto.Customer;
import org.bonn.se2.model.objects.dto.Salesman;
import org.bonn.se2.model.objects.dto.User;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.process.control.exceptions.InvalidCredentialsException;
import org.bonn.se2.services.util.Config;
import org.bonn.se2.services.util.SessionFunctions;
import org.bonn.se2.services.util.UIFunctions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Henry Weckermann
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class UserPageView extends VerticalLayout implements View {

    protected Car selectedCar = null;

    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (!SessionFunctions.isLoggedIn()) {
            UI.getCurrent().getNavigator().navigateTo(Config.Views.LOGIN);
        } else {
            this.parentSetUp();
            try {
                this.setUp();
            } catch (DatabaseException | InvalidCredentialsException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void parentSetUp() {
        NavigationBar navigationBar = new NavigationBar();
        this.addComponent(navigationBar);
        this.setComponentAlignment(navigationBar, Alignment.TOP_CENTER);
    }

    public void setUp() throws DatabaseException, InvalidCredentialsException, SQLException {

        UserDAO userDAO = new UserDAO();
        User user = userDAO.retrieve(SessionFunctions.getCurrentUser().getUserID());

        Button deleteButton = new Button("Löschen");
        deleteButton.setEnabled(false);
        deleteButton.addStyleName("danger");

        Button backButton = new Button("Zurück", e -> {
            UIFunctions.gotoMain();
        });
        backButton.setIcon(VaadinIcons.ARROW_LEFT);

        VerticalLayout spacer = new VerticalLayout();
        Button spaceButton = new Button();
        spaceButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
        spacer.addComponents(spaceButton, spaceButton, spaceButton, spaceButton);


        if (SessionFunctions.getCurrentRole().equals(Config.Roles.CUSTOMER)) {

            CustomerDAO customerDAO = new CustomerDAO();
            Customer customer = customerDAO.retrieve(user.getUserID());

            ReservationDAO reservationDAO = new ReservationDAO();
            List<Integer> resIDs = reservationDAO.retrieveReservationsByCustomerID(customer.getCustomerID());


            if (!(resIDs == null)) {
                List<Car> reservedCars = new ArrayList<>();

                Grid<Car> gridReservation = new Grid<>();
                gridReservation.setSizeFull();
                gridReservation.setHeightMode(HeightMode.UNDEFINED);
                gridReservation.setSelectionMode(Grid.SelectionMode.SINGLE);

                CarDAO carDAO = new CarDAO();

                for (int id : resIDs) {
                    reservedCars.add(carDAO.retrieve(id));
                }

                gridReservation.setItems(reservedCars);
                addGridComponentsCar(gridReservation);

                HorizontalLayout buttonLayout = new HorizontalLayout();
                buttonLayout.addComponents(backButton, deleteButton);
                buttonLayout.setComponentAlignment(deleteButton, Alignment.MIDDLE_CENTER);
                buttonLayout.setComponentAlignment(backButton, Alignment.MIDDLE_LEFT);

                this.addComponents(spacer, gridReservation, buttonLayout);
                this.setComponentAlignment(gridReservation, Alignment.MIDDLE_CENTER);

                deleteButton.addClickListener(e -> {
                    try {
                        ReservationDAO deleteReservationDAO = new ReservationDAO();
                        deleteReservationDAO.deleteByCarID(customer.getCustomerID(), selectedCar.getCarID());
                        Notification successNotification = new Notification("Reservierung erfolgreich gelöscht", Notification.Type.ASSISTIVE_NOTIFICATION);
                        successNotification.setPosition(Position.MIDDLE_CENTER);
                        successNotification.setDelayMsec(3000);
                        successNotification.show(Page.getCurrent());
                        UIFunctions.gotoUserPage();

                    } catch (DatabaseException databaseException) {
                        Logger.getLogger(UserPageView.class.getName()).log(Level.INFO, "Konnte die Reservierung für " + selectedCar + " nicht löschen");
                    }
                });

                gridReservation.addSelectionListener(event -> {
                    if (event.getFirstSelectedItem().isPresent()) {
                        selectedCar = (event.getFirstSelectedItem().get());
                        deleteButton.setEnabled(true);
                        deleteButton.setVisible(true);

                    } else {
                        deleteButton.setEnabled(false);
                    }
                });


            } else {
                Label errNotice = new Label("<h2>Sie haben noch keine Reservierungen gemacht. <br>Sie können auf der Startseite nach Autos suchen und diese für einen Termin reservieren</h2>", ContentMode.HTML);
                errNotice.setStyleName(ValoTheme.LABEL_FAILURE);

                this.addComponent(errNotice);
                this.setComponentAlignment(errNotice, Alignment.MIDDLE_CENTER);

                this.addComponent(backButton);
                this.setComponentAlignment(backButton, Alignment.MIDDLE_CENTER);
            }

        } else {

            SalesmanDAO salesmanDAO = new SalesmanDAO();
            Salesman salesman = salesmanDAO.retrieve(user.getUserID());

            Grid<Car> gridCars = new Grid<>();
            gridCars.setSizeFull();
            gridCars.setHeightMode(HeightMode.UNDEFINED);
            gridCars.setSelectionMode(Grid.SelectionMode.SINGLE);

            CarDAO carDAO = new CarDAO();
            List<Car> liste = carDAO.retrieveCarBySalesman(salesman.getSalesmanID());

            gridCars.setItems(liste);
            addGridComponentsCar(gridCars);

            HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.addComponents(backButton, deleteButton);
            buttonLayout.setComponentAlignment(deleteButton, Alignment.MIDDLE_CENTER);
            buttonLayout.setComponentAlignment(backButton, Alignment.MIDDLE_LEFT);

            this.addComponents(spacer, gridCars, buttonLayout);
            this.setComponentAlignment(gridCars, Alignment.MIDDLE_CENTER);

            deleteButton.addClickListener(e -> {
                try {
                    CarDAO deleteDao = new CarDAO();
                    deleteDao.delete(selectedCar);
                    Notification successNotification = new Notification("Auto wurde erfolgreich gelöscht", Notification.Type.ASSISTIVE_NOTIFICATION);
                    successNotification.setPosition(Position.MIDDLE_CENTER);
                    successNotification.setDelayMsec(3000);
                    successNotification.show(Page.getCurrent());
                    UIFunctions.gotoUserPage();
                } catch (DatabaseException databaseException) {
                    Logger.getLogger(UserPageView.class.getName()).log(Level.INFO, "Konnte das Auto " + selectedCar + " nicht löschen");
                    Notification inUseNotification = new Notification("Es gibt noch mind. eine Reservierung für dieses Auto.", Notification.Type.ERROR_MESSAGE);
                    inUseNotification.setPosition(Position.MIDDLE_CENTER);
                    inUseNotification.setDelayMsec(3000);
                    inUseNotification.show(Page.getCurrent());
                    UIFunctions.gotoUserPage();
                }
            });

            gridCars.addSelectionListener(event -> {
                if (event.getFirstSelectedItem().isPresent()) {
                    selectedCar = (event.getFirstSelectedItem().get());
                    deleteButton.setEnabled(true);
                    deleteButton.setVisible(true);

                } else {
                    deleteButton.setEnabled(false);
                }
            });
        }
    }


    private void addGridComponentsCar(Grid<Car> gridReservation) {
        gridReservation.addColumn(Car::getBrand).setCaption("Marke");
        gridReservation.addColumn(Car::getModel).setCaption("Modell");
        gridReservation.addColumn(Car::getBuildYear).setCaption("Baujahr");
        gridReservation.addColumn(Car::getColor).setCaption("Farbe");
        gridReservation.addColumn(Car::getDescription).setCaption("Beschreibung").setSortable(false);
        gridReservation.addColumn(Car::getPrice).setCaption("Preis");
    }


}
