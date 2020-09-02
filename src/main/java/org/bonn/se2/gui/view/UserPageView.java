package org.bonn.se2.gui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import org.bonn.se2.gui.components.NavigationBar;
import org.bonn.se2.model.dao.CarDAO;
import org.bonn.se2.model.dao.CustomerDAO;
import org.bonn.se2.model.dao.SalesmanDAO;
import org.bonn.se2.model.dao.UserDAO;
import org.bonn.se2.model.objects.dto.Car;
import org.bonn.se2.model.objects.dto.Customer;
import org.bonn.se2.model.objects.dto.Salesman;
import org.bonn.se2.model.objects.dto.User;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.process.control.exceptions.InvalidCredentialsException;
import org.bonn.se2.services.util.Config;
import org.bonn.se2.services.util.SessionFunctions;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserPageView extends VerticalLayout implements View {

    private Customer customer;
    private Salesman salesman;
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

        HorizontalLayout spacer = new HorizontalLayout();
        spacer.addComponents(new Label("&nbsp", ContentMode.HTML), new Label("&nbsp", ContentMode.HTML), new Label("&nbsp", ContentMode.HTML));
        spacer.setSizeFull();


        if (SessionFunctions.getCurrentRole().equals(Config.Roles.CUSTOMER)) {

            CustomerDAO customerDAO = new CustomerDAO();
            customer = customerDAO.retrieve(user.getUserID());

            //TODO HIER

            Grid<Car> gridReservation = new Grid<>();
            gridReservation.setSizeFull();
            gridReservation.setHeightMode(HeightMode.UNDEFINED);
            gridReservation.setSelectionMode(Grid.SelectionMode.NONE);

            CarDAO carDAO = new CarDAO();

            //TODO EHRE
            List<Car> liste = carDAO.retrieveAll();

            gridReservation.setItems(liste);
            addGridComponentsCar(gridReservation);

            this.addComponent(spacer);
            this.addComponents(gridReservation);
            this.setComponentAlignment(gridReservation, Alignment.MIDDLE_CENTER);
            this.setComponentAlignment(deleteButton, Alignment.MIDDLE_CENTER);


        } else {

            SalesmanDAO salesmanDAO = new SalesmanDAO();
            salesman = salesmanDAO.retrieve(user.getUserID());

            Grid<Car> gridCars = new Grid<>();
            gridCars.setSizeFull();
            gridCars.setHeightMode(HeightMode.UNDEFINED);
            gridCars.setSelectionMode(Grid.SelectionMode.SINGLE);

            CarDAO carDAO = new CarDAO();
            List<Car> liste = carDAO.retrieveCarBySalesman(salesman.getSalesmanID());

            gridCars.setItems(liste);
            addGridComponentsCar(gridCars);

            this.addComponent(spacer);
            this.addComponents(gridCars, deleteButton);
            this.setComponentAlignment(gridCars, Alignment.MIDDLE_CENTER);
            this.setComponentAlignment(deleteButton, Alignment.MIDDLE_CENTER);

            deleteButton.addClickListener(e -> {
                try {
                    CarDAO deleteDao = new CarDAO();
                    deleteDao.delete(selectedCar);
                } catch (DatabaseException databaseException) {
                    Logger.getLogger(UserPageView.class.getName()).log(Level.INFO, "Konnte das Auto " + selectedCar + " nicht löschen");
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
