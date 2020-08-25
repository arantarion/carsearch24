package org.bonn.se2.process.control;

import com.vaadin.ui.UI;
import org.bonn.se2.gui.view.SearchView;
import org.bonn.se2.model.dao.CarDAO;
import org.bonn.se2.model.objects.dto.Car;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.services.util.Config;

import java.util.ArrayList;
import java.util.List;

public class SearchControl implements SearchControlInterface {

    private static SearchControlInterface search;

    public void search(String s) throws DatabaseException {

        List<Car> filteredVacancies;

        filteredVacancies = filterCars(s);
        SearchView.setCarHeader(new Car());

        SearchView.setCurrentResults(filteredVacancies);
        SearchView.setAllCars(false);
        UI.getCurrent().getNavigator().navigateTo(Config.Views.SEARCH);
    }


    public List<Car> filterCars(String searchTerm) throws DatabaseException {
        List<Car> cars = new CarDAO().getAll();
        List<Car> filtered = new ArrayList<>();
        cars.stream()
                .filter(car -> car.getBrand().toLowerCase().contains(searchTerm.toLowerCase()))
                .forEach(filtered::add);
        return filtered;
    }

    public List<Car> liveFilterCombobox(String searchTerm) throws DatabaseException {
        List<Car> cars = new CarDAO().getAll();
        List<Car> filtered = new ArrayList<>();
        cars.stream()
                .filter(car -> car.getBrand().toLowerCase().contains(searchTerm.toLowerCase()))
                .forEach(filtered::add);

        return filtered;
    }

    public List<Car> filterResults(String brand, String model, String color) {
        List<Car> cars = SearchView.getCurrentResults();
        List<Car> filtered = new ArrayList<>();

        cars.stream()
                .filter(car -> {
                    if (brand != null) {
                        return car.getBrand().toLowerCase().contains(brand.toLowerCase());
                    }
                    return true;
                })
                .filter(car -> {
                    if (model != null) {
                        return car.getModel().toLowerCase().contains(model.toLowerCase());
                    }
                    return true;
                })
                .filter(car -> {
                    if (color != null) {
                        return car.getColor().toLowerCase().contains(color.toLowerCase());
                    }
                    return true;
                })
                .forEach(filtered::add);
        return filtered;
    }

}
