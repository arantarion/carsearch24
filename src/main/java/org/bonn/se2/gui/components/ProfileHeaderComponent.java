package org.bonn.se2.gui.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.bonn.se2.model.objects.dto.Car;

public class ProfileHeaderComponent extends VerticalLayout {

    private final GridLayout profileHeaderGridLayout = new GridLayout(4, 5);

    public ProfileHeaderComponent(Car car) {
        setUp(car);
    }

    private void setUp(Car dto) {
        this.setWidth("100%");
        this.addStyleName("profile-components");
        this.addStyleNames("profileheader-components");
        this.addComponent(profileHeaderGridLayout);
        this.setComponentAlignment(profileHeaderGridLayout, Alignment.MIDDLE_CENTER);
        String current = UI.getCurrent().getNavigator().getCurrentView().getClass().toString().toLowerCase();

        profileHeaderGridLayout.setWidth("80%");

        Label brand = createLabel("Brand: " + dto.getBrand());
        Label model = createLabel("Model: " + dto.getModel() + " from: " + dto.getBuildYear(), VaadinIcons.CAR);
        Label color = createLabel("Color: " + dto.getColor(), VaadinIcons.PALETE);
        Label description = createLabel("Description: " + dto.getDescription(), VaadinIcons.LIST);
        Label price = createLabel("Price: " + dto.getPrice(), VaadinIcons.MONEY);

        profileHeaderGridLayout.addComponent(brand, 1, 1);
        profileHeaderGridLayout.addComponent(model, 1, 2);
        profileHeaderGridLayout.addComponent(color, 2, 0);
        profileHeaderGridLayout.addComponent(description, 2, 1);
        profileHeaderGridLayout.addComponent(price, 2, 2);

        profileHeaderGridLayout.setColumnExpandRatio(0, 0.25F);
        profileHeaderGridLayout.setColumnExpandRatio(1, 4F);
        profileHeaderGridLayout.setColumnExpandRatio(2, 4F);
        profileHeaderGridLayout.setColumnExpandRatio(3, 1F);

        setComponentAlignmentMiddleLeft();
    }

    private void setComponentAlignmentMiddleLeft() {
        for (Component comp : profileHeaderGridLayout) {
            if (!(comp instanceof Button)) {
                profileHeaderGridLayout.setComponentAlignment(comp, Alignment.MIDDLE_LEFT);
            }
        }
    }

    private Label createLabel(String s_label) {
        Label l_label = new Label(s_label);
        l_label.setWidth("100%");
        return l_label;
    }

    private Label createLabel(String s_label, VaadinIcons icon) {
        Label l_label = new Label(icon.getHtml() + "&nbsp;" + s_label, ContentMode.HTML);
        l_label.setWidth("100%");
        return l_label;
    }

}
