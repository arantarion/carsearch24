package org.bonn.se2.process.control.proxy;

import org.bonn.se2.model.objects.dto.Car;
import org.bonn.se2.process.control.SearchControl;
import org.bonn.se2.process.control.exceptions.DatabaseException;

import java.util.List;

public class SearchProxy implements SearchInterface {

    private SearchControl search;

    public SearchProxy() {
        search = new SearchControl();
    }

    @Override
    public void search(String s) throws DatabaseException {
        search.search(s);
    }

    @Override
    public List<Car> filterCars(String searchTerm) throws DatabaseException {
        return search.filterCars(searchTerm);
    }

    @Override
    public List<Car> filterResults(String brand, String model, String color) throws DatabaseException {
        return search.filterResults(brand, model, color);
    }

    @Override
    public List<Car> liveFilterCombobox(String searchTerm) throws DatabaseException {
        return search.liveFilterCombobox(searchTerm);
    }

}
