package org.bonn.se2.gui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import org.bonn.se2.gui.components.CarFilterComponent;
import org.bonn.se2.gui.components.CarResultComponent;
import org.bonn.se2.gui.components.ProfileHeaderComponent;
import org.bonn.se2.model.objects.dto.Car;
import org.bonn.se2.services.util.Config;
import org.bonn.se2.services.util.SessionFunctions;
import org.bonn.se2.services.util.Util;

import java.util.ArrayList;
import java.util.List;

public class SearchView extends MainView implements View {

    private static List<Car> currentResults, filteredResults;

    public static List<Car> getCurrentResults() {
        return SearchView.currentResults;
    }

    public static void setCurrentResults(List<Car> currentResults) {
        SearchView.currentResults = currentResults;
    }

    public static void setFilteredResults(List<Car> filteredResults) {
        SearchView.filteredResults = filteredResults;
    }

    private static Car carHeader;

    public static void setCarHeader(Car carHeader) {
        SearchView.carHeader = carHeader;
    }

    private static HorizontalLayout resultsLayout;

    private static boolean allCars = false;

    public static void setAllCars(boolean bool) {
        allCars = bool;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        if (!SessionFunctions.isLoggedIn()) {
            UI.getCurrent().getNavigator().navigateTo(Config.Views.LOGIN);
        } else {
            this.parentSetUp();
            setUpSearch();
        }
    }

    private void setUpSearch() {
        this.addStyleName("views");
        this.addStyleName("searchview");
        this.setSizeFull();

        GridLayout layout = new GridLayout();
        layout.addStyleName("searchview-gridlayout");
        layout.setSpacing(true);
        layout.setSizeFull();
        this.addComponent(layout);

        if (carHeader.getBrand() != null) {
            HorizontalLayout compHorLayout = new HorizontalLayout();
            compHorLayout.setWidth("100%");
            layout.addComponent(compHorLayout);

            ProfileHeaderComponent company = new ProfileHeaderComponent(carHeader);
            compHorLayout.addComponent(company);
        }

        if (currentResults.size() > 0) {
            HorizontalLayout filterHorLayout = new HorizontalLayout();
            filterHorLayout.setWidth("100%");
            layout.addComponent(filterHorLayout);

            CarFilterComponent filter = new CarFilterComponent();
            filterHorLayout.addComponent(filter);

            resultsLayout = new HorizontalLayout();
            resultsLayout.setWidth("100%");
            layout.addComponent(resultsLayout);
            String title = "Ergebnisse der Suche";
            if (allCars) title = "All Cars";
            reloadResults(currentResults, title);
        } else {
            HorizontalLayout noResultsLayout = Util.createNoResultsWarning("Zu der angegebenen Suche konnten leider keine Autos gefunden werden!");
            layout.addComponent(noResultsLayout);

        }
    }

    public static void reloadResults(List<Car> resultList, String title) {
        resultsLayout.removeAllComponents();
        CarResultComponent results = null;
        try {
            results = new CarResultComponent((ArrayList<Car>) resultList, title);
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultsLayout.addComponent(results);
    }

}
