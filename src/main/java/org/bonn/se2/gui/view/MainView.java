package org.bonn.se2.gui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import org.bonn.se2.gui.components.NavigationBar;
import org.bonn.se2.gui.windows.CarCreationWindow;
import org.bonn.se2.model.dao.CarDAO;
import org.bonn.se2.model.objects.dto.Car;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.services.util.Config;
import org.bonn.se2.services.util.SessionFunctions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MainView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        if (!SessionFunctions.isLoggedIn()) {
            UI.getCurrent().getNavigator().navigateTo(Config.Views.LOGIN);
        } else {
            this.parentSetUp();
            try {
                this.setUp();
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }
    }

    public void parentSetUp() {
        NavigationBar navigationBar = new NavigationBar();
        this.addComponent(navigationBar);
        this.setComponentAlignment(navigationBar, Alignment.TOP_CENTER);
    }

    public void setUp() throws DatabaseException {

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        HorizontalLayout horizontalLayoutCompany = new HorizontalLayout();
        HorizontalLayout h2 = new HorizontalLayout();

        //Button suche = new Button("Suchen", VaadinIcons.SEARCH);
        Button carCreation = new Button("Auto erstellen", VaadinIcons.CAR);
        TextField searchField = new TextField();
        searchField.setPlaceholder("Suchen...");
        searchField.addStyleName("inline-icon");
        searchField.setIcon(VaadinIcons.SEARCH);
        searchField.setWidth("500px");

        //Label label = new Label("Bitte geben Sie ein Stichwort ein:");

//        addComponent(horizontalLayout);
//        setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
//
//        addComponent(h2);
//        setComponentAlignment(h2, Alignment.TOP_LEFT);
//
//        addComponent(horizontalLayoutCompany);

        addComponents(horizontalLayout, h2, horizontalLayoutCompany);
        setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
        setComponentAlignment(h2, Alignment.TOP_LEFT);
        setComponentAlignment(horizontalLayoutCompany, Alignment.MIDDLE_CENTER);

        //horizontalLayoutCompany.addComponent(label);
        horizontalLayoutCompany.addComponent(searchField);
        horizontalLayoutCompany.addComponent(new Label("&nbsp", ContentMode.HTML));

        if (SessionFunctions.getCurrentRole().equals(Config.Roles.SALESMAN)) {
            horizontalLayoutCompany.addComponent(carCreation);
        }

        carCreation.addClickListener((Button.ClickListener) clickEvent -> {
            Window create = new CarCreationWindow();
            UI.getCurrent().addWindow(create);
        });

        Grid<Car> grid = new Grid<>();

        CarDAO carDAO = new CarDAO();
        List<Car> liste = carDAO.retrieveAll();

        grid.setSizeFull();
        grid.setHeightMode(HeightMode.UNDEFINED);

        grid.setItems(liste);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        addGridComponents(grid);
        this.addComponent(grid);
        this.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);

        //searchBox.getSearchField().addValueChangeListener()
        // Show selected item

        //searchBox.addSearchListener(e -> {
        searchField.addValueChangeListener(e -> {
            if (!searchField.getValue().equals("")) {
                grid.removeAllColumns();

                String attr = searchField.getValue();
                List<Car> liste2 = null;
                try {
                    liste2 = carDAO.retrieveCar(attr);
                } catch (DatabaseException | SQLException databaseException) {
                    databaseException.printStackTrace();
                }

                grid.setItems(liste2);

            } else {
                grid.removeAllColumns();

                List<Car> liste3 = null;

                try {
                    liste3 = carDAO.retrieveAll();
                } catch (DatabaseException databaseException) {
                    databaseException.printStackTrace();
                }

                grid.setItems(liste3);
            }
            addGridComponents(grid);
        });

    }

    private List<Car> suggestCarBrand(String query, int cap) {

        List<Car> carList = new ArrayList<>();

        try {
            carList = new CarDAO().retrieveAll();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        return carList.stream()
                .filter(p -> p.contains(query))
                .limit(cap).collect(Collectors.toList());
    }

    private String convertValueCar(Car car) {
        return car.getBrand() + " " + car.getModel() + " aus dem Jahr " + car.getBuildYear();
    }

    private void addGridComponents(Grid<Car> grid) {
        grid.addColumn(Car::getBrand).setCaption("Marke");
        grid.addColumn(Car::getModel).setCaption("Modell");
        grid.addColumn(Car::getBuildYear).setCaption("Baujahr");
        grid.addColumn(Car::getColor).setCaption("Farbe");
        grid.addColumn(Car::getDescription).setCaption("Beschreibung");
        grid.addColumn(Car::getPrice).setCaption("Preis");
        grid.addComponentColumn(jobOffer -> {
            Button button;
            button = new Button("Reservieren");
            button.addClickListener(click -> {
                Notification.show("Reserviert");
            });
            return button;
        }).setCaption("Reservieren");
    }

}
