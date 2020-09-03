package org.bonn.se2.gui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;
import org.bonn.se2.gui.components.NavigationBar;
import org.bonn.se2.gui.components.TextFieldWithButton;
import org.bonn.se2.gui.windows.CarCreationWindow;
import org.bonn.se2.gui.windows.ReserveCarWindow;
import org.bonn.se2.model.dao.CarDAO;
import org.bonn.se2.model.objects.dto.Car;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.services.util.Config;
import org.bonn.se2.services.util.SessionFunctions;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Henry Weckermann, Anton Drees
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class MainView extends VerticalLayout implements View {

    // The car that is selected in the grid
    protected Car selectedCar = null;

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

        // custom style for background image
        addStyleName("background");

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        HorizontalLayout horizontalLayoutCompany = new HorizontalLayout();
        HorizontalLayout h2 = new HorizontalLayout();

        Button carCreation = new Button("Auto erstellen", VaadinIcons.CAR);

        addComponents(horizontalLayout, h2, horizontalLayoutCompany);
        setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
        setComponentAlignment(h2, Alignment.TOP_LEFT);
        setComponentAlignment(horizontalLayoutCompany, Alignment.MIDDLE_CENTER);

        TextFieldWithButton fieldWithButton = new TextFieldWithButton();
        horizontalLayoutCompany.addComponent(fieldWithButton);
        horizontalLayoutCompany.addComponent(new Label("&nbsp", ContentMode.HTML));

        // add the "Auto erstellen" Button if the user is a Salesman
        if (SessionFunctions.getCurrentRole().equals(Config.Roles.SALESMAN)) {
            horizontalLayoutCompany.addComponent(carCreation);
        }

        carCreation.addClickListener((Button.ClickListener) clickEvent -> {
            Window create = new CarCreationWindow();
            UI.getCurrent().addWindow(create);
        });

        // -----------------------------------------
        // The grid that displays the search results
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

        Button reserveButton = new Button("Reservieren");
        // make sure that the button is only enabled when a result is selected in the grid
        reserveButton.setEnabled(false);

        this.addComponent(reserveButton);
        this.setComponentAlignment(reserveButton, Alignment.MIDDLE_CENTER);

        reserveButton.addClickListener(e -> {
            Window create = null;
            try {
                create = new ReserveCarWindow(selectedCar);
            } catch (DatabaseException databaseException) {
                Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                        new Throwable().getStackTrace()[0].getMethodName() + " failed", databaseException);
            }
            UI.getCurrent().addWindow(create);
        });

        // reduce the car object to its toString representation and split all at the space character
        Collection<String> suggestions = liste.stream()
                .map(Car::toString) // use toString representation
                .map(w -> w.split("\\s+")) // split at space
                .flatMap(Arrays::stream) // create only one list
                .distinct() // use only distinct values. looks better
                .collect(Collectors.toList()); // Collect to a list

        // Set the suggestionprovider
        AutocompleteSuggestionProvider suggestionProvider = new CollectionSuggestionProvider(suggestions, MatchMode.BEGINS, true, Locale.GERMAN);

        fieldWithButton.getTextField().setDelay(150);           // after 150ms server responds with results
        fieldWithButton.getTextField().setItemAsHtml(false);
        fieldWithButton.getTextField().setMinChars(3);          // user must specify at least 3 chars for results
        fieldWithButton.getTextField().setScrollBehavior(ScrollBehavior.NONE);
        fieldWithButton.getTextField().setSuggestionProvider(suggestionProvider);

        fieldWithButton.getTextField().addValueChangeListener(e -> {
            // if user has inputted a search term
            if (!fieldWithButton.getTextField().getValue().equals("")) {

                grid.removeAllColumns();
                String attr = fieldWithButton.getTextField().getValue();
                List<Car> liste2 = null;
                try {
                    liste2 = carDAO.retrieveCar(attr);
                } catch (DatabaseException | SQLException databaseException) {
                    Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                            new Throwable().getStackTrace()[0].getMethodName() + " failed", databaseException);
                }
                grid.setItems(liste2);

                // if the field is empty -> display all cars in the database
            } else {

                grid.removeAllColumns();
                List<Car> liste3 = null;
                try {
                    liste3 = carDAO.retrieveAll();
                } catch (DatabaseException databaseException) {
                    Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                            new Throwable().getStackTrace()[0].getMethodName() + " failed", databaseException);
                }
                grid.setItems(liste3);
            }
            addGridComponents(grid);
        });

        // Get selected grid cell and activate the "Reservieren" button.
        grid.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().isPresent()) {
                selectedCar = (event.getFirstSelectedItem().get());
                //Notification if salesman tries to select an item in the grid
                if (SessionFunctions.getCurrentRole().equals(Config.Roles.SALESMAN)) {
                    Notification errNotification = new Notification("Nur Kunden können ein Auto reservieren", Notification.Type.ERROR_MESSAGE);
                    errNotification.setDelayMsec(3000);
                    errNotification.setPosition(Position.BOTTOM_CENTER);
                    errNotification.show(Page.getCurrent());
                } else {
                    reserveButton.setEnabled(true);
                }
            } else {
                reserveButton.setEnabled(false);
            }
        });
    }

    // Helper method to populate the grid
    private void addGridComponents(Grid<Car> grid) {
        grid.addColumn(Car::getBrand).setCaption("Marke");
        grid.addColumn(Car::getModel).setCaption("Modell");
        grid.addColumn(Car::getBuildYear).setCaption("Baujahr");
        grid.addColumn(Car::getColor).setCaption("Farbe");
        grid.addColumn(Car::getDescription).setCaption("Beschreibung").setSortable(false);
        grid.addColumn(Car::getPrice).setCaption("Preis");
    }

}
