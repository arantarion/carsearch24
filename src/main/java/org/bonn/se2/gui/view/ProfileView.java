package org.bonn.se2.gui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import org.bonn.se2.gui.components.NavigationBar;
import org.bonn.se2.model.objects.dto.Customer;
import org.bonn.se2.model.objects.dto.Salesman;
import org.bonn.se2.services.util.Config;
import org.bonn.se2.services.util.SessionFunctions;
import org.bonn.se2.services.util.UIFunctions;


public class ProfileView extends VerticalLayout implements View {

    private static Customer customer;
    private static Salesman salesman;
    private static boolean myProfile = true;
    private GridLayout layout;

    public static Customer getCustomer() {
        return customer;
    }

    public static void setCustomer(Customer customer) {
        ProfileView.customer = customer;
    }

    public static Salesman getSalesman() {
        return salesman;
    }

    public static void setSalesman(Salesman salesman) {
        ProfileView.salesman = salesman;
    }

    public static boolean isMyProfile() {
        return myProfile;
    }

    public static void setMyProfile(boolean myProfile) {
        ProfileView.myProfile = myProfile;
    }

    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (!SessionFunctions.isLoggedIn()) {
            UIFunctions.gotoLogin();
        } else {
            this.setUp();
        }
    }

    public void setUp() {

        layout = new GridLayout(1, 3); //
        layout.setSpacing(true);
        layout.setSizeFull();
        this.addComponent(layout);

        layout.setColumnExpandRatio(0, 0.5f);

        NavigationBar navigationBar = new NavigationBar();

        layout.addComponent(navigationBar, 0, 0);
        layout.setComponentAlignment(navigationBar, Alignment.TOP_CENTER);

        if (SessionFunctions.getCurrentRole().equals(Config.Roles.CUSTOMER)) {
            //TODO
        } else {
            //TODO
        }
    }


}
