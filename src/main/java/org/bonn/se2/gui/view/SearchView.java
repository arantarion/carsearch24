package org.bonn.se2.gui.view;

import com.vaadin.navigator.View;

public class SearchView extends MainView implements View {

//    private static List<Car> currentResults, filteredResults;
//    private static Car carHeader;
//    private static HorizontalLayout resultsLayout;
//    private static boolean allCars = false;
//
//    public static List<Car> getCurrentResults() {
//        return SearchView.currentResults;
//    }
//
//    public static void setCurrentResults(List<Car> currentResults) {
//        SearchView.currentResults = currentResults;
//    }
//
//    public static void setFilteredResults(List<Car> filteredResults) {
//        SearchView.filteredResults = filteredResults;
//    }
//
//    public static void setCarHeader(Car carHeader) {
//        SearchView.carHeader = carHeader;
//    }
//
//    public static void setAllCars(boolean bool) {
//        allCars = bool;
//    }
//
//    public static void reloadResults(List<Car> resultList, String title) {
//        resultsLayout.removeAllComponents();
//        CarResultComponent results = null;
//        try {
//            results = new CarResultComponent((ArrayList<Car>) resultList, title);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        resultsLayout.addComponent(results);
//    }
//
//    @Override
//    public void enter(ViewChangeListener.ViewChangeEvent event) {
//
//        if (!SessionFunctions.isLoggedIn()) {
//            UI.getCurrent().getNavigator().navigateTo(Config.Views.LOGIN);
//        } else {
//            this.parentSetUp();
//            setUpSearch();
//        }
//    }
//
//    private void setUpSearch() {
//        this.addStyleName("views");
//        this.addStyleName("searchview");
//        this.setSizeFull();
//
//        GridLayout layout = new GridLayout();
//        layout.addStyleName("searchview-gridlayout");
//        layout.setSpacing(true);
//        layout.setSizeFull();
//        this.addComponent(layout);
//
//        if (carHeader.getBrand() != null) {
//            HorizontalLayout compHorLayout = new HorizontalLayout();
//            compHorLayout.setWidth("100%");
//            layout.addComponent(compHorLayout);
//
//            ProfileHeaderComponent company = new ProfileHeaderComponent(carHeader);
//            compHorLayout.addComponent(company);
//        }
//
//        if (currentResults.size() > 0) {
//            HorizontalLayout filterHorLayout = new HorizontalLayout();
//            filterHorLayout.setWidth("100%");
//            layout.addComponent(filterHorLayout);
//
//            CarFilterComponent filter = new CarFilterComponent();
//            filterHorLayout.addComponent(filter);
//
//            resultsLayout = new HorizontalLayout();
//            resultsLayout.setWidth("100%");
//            layout.addComponent(resultsLayout);
//            String title = "Ergebnisse der Suche";
//            if (allCars) title = "All Cars";
//            reloadResults(currentResults, title);
//        } else {
//            HorizontalLayout noResultsLayout = Util.createNoResultsWarning("Zu der angegebenen Suche konnten leider keine Autos gefunden werden!");
//            layout.addComponent(noResultsLayout);
//
//        }
//    }

}
