package org.bonn.se2.gui.windows;

import com.vaadin.ui.*;
import org.bonn.se2.model.objects.dto.Car;

public class ReserveCarWindow extends Window {

    private Car car ;
    public ReserveCarWindow(Car car){
        this.car = car;
        setUp();
    }

    private void setUp(){

        VerticalLayout reservationLayout = new VerticalLayout();

        Label title = new Label("Wollen Sie wirklich dieses Auto reservieren?");

        Label brand = new Label("Marke: " + car.getBrand());
        Label buildYear = new Label("Baujahr: " + car.getBuildYear());
        Label color = new Label("Farbe: " + car.getColor());
        Label price = new Label("Preis: " + car.getPrice());
        Label model = new Label("Modell: " + car.getModel());

        brand.setSizeFull();
        model.setSizeFull();
        buildYear.setSizeFull();
        color.setSizeFull();
        price.setSizeFull();

        reservationLayout.addComponents(title,brand,model,buildYear,color,price);

        this.setContent(reservationLayout);
        this.setWidth("25%");
        this.center();
    }
}
