package org.bonn.se2.gui.components;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import org.bonn.se2.model.objects.dto.Car;
import org.bonn.se2.services.util.Notifier;


public class HorizontalCardsComponent extends HorizontalLayout {

    private final GridLayout horizontalCard = new GridLayout(2, 4);

    HorizontalCardsComponent(Car dto) {
        this.addStyleName("horizontalcard");
        setUp(dto);
    }


    private void setUp(Car dto) {
        Panel p = new Panel();
        p.setWidth("430px");
        p.setHeight("100px");
        horizontalCard.setSizeFull();
        
        Label nameLabel = new Label(dto.getBrand() + " " + dto.getModel() + " from " + dto.getBuildYear());
        nameLabel.setWidth("320px");
        nameLabel.addStyleName("horizontalcard-namelabel");
        horizontalCard.addComponent(nameLabel, 1, 2);

        Button carButton;

        if (nameLabel.getValue().length() > 30) {
            carButton = new Button(nameLabel.getValue().substring(0, 30) + "...");
        } else {
            carButton = new Button(nameLabel.getValue());
        }

        carButton.setWidth("330px");
        carButton.addStyleName("horizontalcard-button");

        carButton.addClickListener((Button.ClickListener) click -> {
            CarResultComponent.detailsLayout.removeAllComponents();
            try {
                CarResultComponent.detailsLayout.addComponent(CarResultComponent.createDetailsCard(dto));

            } catch (Exception e) {
                new Notifier().createErrorNotification("Die Details der Autos konnten nicht gefunden werden. Bitte kontaktieren Sie den Administrator.")
                        .at(Position.TOP_CENTER)
                        .show();
                e.printStackTrace();
            }
            CarResultComponent.detailsLayout.setVisible(true);
        });
        horizontalCard.addComponent(carButton, 1, 0);
        horizontalCard.setComponentAlignment(carButton, Alignment.TOP_RIGHT);


        horizontalCard.setColumnExpandRatio(0, 2.5F);

        p.setContent(horizontalCard);
        this.addComponent(p);
    }
}
