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
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
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
import java.util.stream.Collectors;

/**
 * @author Henry Weckermann, Anton Drees
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class MainView extends VerticalLayout implements View {

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

        addStyleName("background");

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        HorizontalLayout horizontalLayoutCompany = new HorizontalLayout();
        HorizontalLayout h2 = new HorizontalLayout();

        Button carCreation = new Button("Auto erstellen", VaadinIcons.CAR);

        AutocompleteTextField autoSearchField = new AutocompleteTextField();
        autoSearchField.setPlaceholder("Suchen...");
        autoSearchField.addStyleName("inline-icon");
        autoSearchField.setIcon(VaadinIcons.SEARCH);
        autoSearchField.setWidth("600px");

        Button deleteSearchButton = new Button("x");
        deleteSearchButton.addStyleNames(ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_TINY);

        addComponents(horizontalLayout, h2, horizontalLayoutCompany);
        setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
        setComponentAlignment(h2, Alignment.TOP_LEFT);
        setComponentAlignment(horizontalLayoutCompany, Alignment.MIDDLE_CENTER);

        TextFieldWithButton fieldWithButton = new TextFieldWithButton();
        horizontalLayoutCompany.addComponent(fieldWithButton);
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

        Button reserveButton = new Button("Reservieren");
        reserveButton.setEnabled(false);
        this.addComponent(reserveButton);
        this.setComponentAlignment(reserveButton, Alignment.MIDDLE_CENTER);

        reserveButton.addClickListener(e -> {
            Window create = null;
            try {
                create = new ReserveCarWindow(selectedCar);
            } catch (DatabaseException databaseException) {
                databaseException.printStackTrace();
            }
            UI.getCurrent().addWindow(create);
        });


        Collection<String> suggestions = liste.stream()
                .map(Car::toString)
                .map(w -> w.split("\\s+"))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());

        AutocompleteSuggestionProvider suggestionProvider = new CollectionSuggestionProvider(suggestions, MatchMode.BEGINS, true, Locale.GERMAN);

        fieldWithButton.getTextField().setDelay(150);
        fieldWithButton.getTextField().setItemAsHtml(false);
        fieldWithButton.getTextField().setMinChars(3);
        fieldWithButton.getTextField().setScrollBehavior(ScrollBehavior.NONE);
        fieldWithButton.getTextField().setSuggestionProvider(suggestionProvider);

        fieldWithButton.getTextField().addValueChangeListener(e -> {
            if (!fieldWithButton.getTextField().getValue().equals("")) {

                grid.removeAllColumns();
                String attr = fieldWithButton.getTextField().getValue();
                List<Car> liste2 = null;
                try {
                    liste2 = carDAO.retrieveCar(attr);
                    liste2.forEach(System.out::println);
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

        grid.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().isPresent()) {
                selectedCar = (event.getFirstSelectedItem().get());
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

    private void addGridComponents(Grid<Car> grid) {
        grid.addColumn(Car::getBrand).setCaption("Marke");
        grid.addColumn(Car::getModel).setCaption("Modell");
        grid.addColumn(Car::getBuildYear).setCaption("Baujahr");
        grid.addColumn(Car::getColor).setCaption("Farbe");
        grid.addColumn(Car::getDescription).setCaption("Beschreibung").setSortable(false);
        grid.addColumn(Car::getPrice).setCaption("Preis");
    }

}
