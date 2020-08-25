package org.bonn.se2.gui.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.bonn.se2.model.objects.dto.Car;

import java.util.ArrayList;

public class CarResultComponent extends VerticalLayout {

    static VerticalLayout detailsLayout = new VerticalLayout();

    public CarResultComponent(ArrayList<Car> list, String title) {
        this.setWidth("100%");
        this.addStyleName("search-components");

        GridLayout carResults = new GridLayout(2, 1);
        carResults.setWidth("80%");
        carResults.setColumnExpandRatio(0, 4F);
        carResults.setColumnExpandRatio(1, 6F);
        this.addComponent(carResults);
        this.setComponentAlignment(carResults, Alignment.MIDDLE_CENTER);

        VerticalLayout carLayout = new VerticalLayout();
        carResults.addComponent(carLayout);

        Label carHeader = new Label(title + " (" + list.size() + ")");
        carLayout.addComponent(carHeader);

        for (Car dto : list) {
            carLayout.addComponent(new HorizontalCardsComponent(dto));
        }

        carResults.addComponent(detailsLayout);
        detailsLayout.setVisible(false);
        detailsLayout.setWidth("100%");
    }

    static Panel createDetailsCard(Car dto) {
        Panel p = new Panel();
        Label title;
        Button detailsButton = new Button("Details");

        GridLayout all = new GridLayout(1, 2);

        all.setWidth("100%");

        GridLayout details = new GridLayout(4, 7);
        details.addStyleName("gridlayout-detailscard");
        details.setWidth("100%");
        details.setColumnExpandRatio(0, 1F);
        details.setColumnExpandRatio(1, 9F);
        all.addComponent(details, 0, 0);

        Label detailsHeader = new Label("Car details");
        detailsLayout.addComponent(detailsHeader);


        title = new Label(dto.getBrand() + " " + dto.getModel() + " from " + dto.getBuildYear());

        Label color = new Label(VaadinIcons.PALETE + dto.getColor(), ContentMode.HTML);
        details.addComponent(color, 1, 1);

        Label price = new Label(VaadinIcons.MONEY + dto.getPrice(), ContentMode.HTML);
        details.addComponent(price, 1, 2);

        Label descTitle = new Label("Beschreibung");
        details.addComponent(descTitle, 0, 4);

        Label description = new Label(dto.getDescription());
        description.setWidth("100%");
        details.addComponent(description, 0, 5, 2, 5);

        title.setWidth("100%");
        details.addComponent(title, 1, 0);

        detailsButton.setWidth("100%");
        all.addComponent(detailsButton, 0, 1);

        p.setContent(all);
        return p;
    }

}
