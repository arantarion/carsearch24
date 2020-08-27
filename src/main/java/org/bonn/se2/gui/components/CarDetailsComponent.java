package org.bonn.se2.gui.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.bonn.se2.model.objects.dto.Car;
import org.bonn.se2.process.control.exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDetailsComponent extends VerticalLayout {

    public CarDetailsComponent(Car vDTO) {
        this.setSizeFull();
        this.setSpacing(true);
        this.addStyleName("vacancy-components");

        GridLayout headerLayout = new GridLayout(4, 6);
        headerLayout.setWidth("80%");
        headerLayout.setColumnExpandRatio(0, 2.5F);
        headerLayout.setColumnExpandRatio(1, 6F);
        headerLayout.setColumnExpandRatio(2, 0.5F);
        headerLayout.setColumnExpandRatio(3, 0.1F);
        this.addComponent(headerLayout);
        this.setComponentAlignment(headerLayout, Alignment.MIDDLE_CENTER);

        Label title = new Label(vDTO.getBrand() + " " + vDTO.getModel());
        title.setWidth("100%");
        headerLayout.addComponent(title, 0, 0, 1, 0);

        GridLayout carBody = new GridLayout(2, 3);
        carBody.setWidth("80%");
        carBody.setSpacing(true);
        this.addComponent(carBody);
        this.setComponentAlignment(carBody, Alignment.MIDDLE_CENTER);

        Panel descriptionPanel;
        if (vDTO.getDescription().isEmpty()) {
            descriptionPanel = createPanel("There is no description available", "Description");
        } else {
            descriptionPanel = createPanel(vDTO.getDescription(), "Description");
        }
        descriptionPanel.setWidth("100%");
        carBody.addComponent(descriptionPanel, 0, 0, 1, 0);


        Panel buildYearPanel;
        if (vDTO.getBuildYear() == null) {
            buildYearPanel = createPanel("There is no build year available", "Build Year");
        } else {
            buildYearPanel = createPanel(vDTO.getBuildYear(), "Build Year");
        }
        buildYearPanel.setWidth("100%");
        carBody.addComponent(buildYearPanel, 0, 1);


        Panel colorPanel;
        if (vDTO.getColor() == null) {
            colorPanel = createPanel("No color available", "Color");
        } else {
            colorPanel = createPanel(vDTO.getColor(), "Color");
        }
        colorPanel.setWidth("100%");
        carBody.addComponent(colorPanel, 1, 1);


        Panel pricePanel;
        if (vDTO.getPrice() == null) {
            pricePanel = createPanel("No price available", "Price");
        } else {
            pricePanel = createPanel(vDTO.getPrice(), "Price");
        }
        pricePanel.setWidth("100%");
        carBody.addComponent(pricePanel, 0, 2, 1, 2);
    }

    private <T> Panel createPanel(T content, String title) {
        Panel panel = new Panel();
        panel.addStyleName("vacancydetails-panel");
        VerticalLayout layout = new VerticalLayout();
        Label l = new Label(title);
        l.setWidth("100%");
        layout.addComponent(l);

        if (content instanceof String) {
            Label lcontent = new Label((String) content);
            lcontent.setWidth("100%");
            layout.addComponent(lcontent);
        } else if (content instanceof List) {
            for (String inter : (ArrayList<String>) content) {
                Label l1 = new Label(VaadinIcons.ARROWS_LONG_RIGHT.getHtml() + " " + inter, ContentMode.HTML);
                l1.setWidth("100%");
                layout.addComponent(l1);
            }
        }

        panel.setContent(layout);
        return panel;
    }

}
