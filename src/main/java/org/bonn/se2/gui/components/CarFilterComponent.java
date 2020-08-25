package org.bonn.se2.gui.components;

import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import org.bonn.se2.gui.view.SearchView;
import org.bonn.se2.model.objects.dto.Car;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.process.control.proxy.SearchInterface;
import org.bonn.se2.process.control.proxy.SearchProxy;
import org.bonn.se2.services.util.Notifier;

import java.util.ArrayList;
import java.util.List;

public class CarFilterComponent extends VerticalLayout {

    public CarFilterComponent() {

        this.addStyleName("search-components");
        this.setWidth("100%");
        this.setSpacing(true);

        Panel filterPanel = new Panel();
        filterPanel.setWidth("80%");
        filterPanel.addStyleName("gridlayouts");
        this.addComponent(filterPanel);
        this.setComponentAlignment(filterPanel, Alignment.MIDDLE_CENTER);

        GridLayout horGrid = new GridLayout(5, 2);
        horGrid.addStyleName("companycomponent-gridlayout");
        horGrid.setWidth("100%");
        filterPanel.setContent(horGrid);

        horGrid.setColumnExpandRatio(0, 2.5F);
        horGrid.setColumnExpandRatio(1, 2.5F);
        horGrid.setColumnExpandRatio(2, 2.5F);
        horGrid.setColumnExpandRatio(3, 2F);
        horGrid.setColumnExpandRatio(4, 0.5F);

        Label header = new Label("Filter");
        horGrid.addComponent(header, 0, 0);

        TextField brand = new TextField();
        brand.setPlaceholder("Brand");
        horGrid.addComponent(brand, 0, 1);
        horGrid.setComponentAlignment(brand, Alignment.MIDDLE_CENTER);

        TextField model = new TextField();
        model.setPlaceholder("Model");
        horGrid.addComponent(model, 1, 1);
        horGrid.setComponentAlignment(model, Alignment.MIDDLE_CENTER);

        TextField color = new TextField();
        color.setPlaceholder("Color");
        horGrid.addComponent(model, 2, 1);
        horGrid.setComponentAlignment(model, Alignment.MIDDLE_CENTER);

        Button deleteFilter = new Button("Delete all filters");
        horGrid.addComponent(deleteFilter, 3, 1);
        horGrid.setComponentAlignment(deleteFilter, Alignment.MIDDLE_RIGHT);
        deleteFilter.setVisible(false);
        deleteFilter.addClickListener((Button.ClickListener) event -> {
            brand.clear();
            model.clear();
            color.clear();
            deleteFilter.setVisible(false);
            SearchView.reloadResults(SearchView.getCurrentResults(), "Search results");
        });

        Button filter = new Button("Filter");
        horGrid.addComponent(filter, 4, 1);
        horGrid.setComponentAlignment(filter, Alignment.MIDDLE_RIGHT);

        SearchInterface search = new SearchProxy();
        filter.addClickListener((Button.ClickListener) click -> {
            List<Car> filtered = new ArrayList<>();

            if (brand.getValue().isEmpty() && model.getValue().isEmpty() && color.getValue() == null) {
                new Notifier().createNotification("Please select a filter")
                        .at(Position.TOP_CENTER)
                        .show();
            } else {
                try {
                    filtered = search.filterResults(brand.getValue(), model.getValue(), color.getValue());
                } catch (DatabaseException e) {
                    new Notifier().createErrorNotification("Can't filter. Please contact an administrator")
                            .at(Position.TOP_CENTER)
                            .show();
                    e.printStackTrace();
                }

                if (filtered.size() == 0) {
                    new Notifier().createNotification("No results!")
                            .at(Position.TOP_CENTER)
                            .show();
                } else {
                    SearchView.setFilteredResults(filtered);
                    SearchView.reloadResults(filtered, "Results");
                }
                deleteFilter.setVisible(true);
            }
        });
    }
}
