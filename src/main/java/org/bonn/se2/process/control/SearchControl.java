package org.bonn.se2.process.control;

import org.bonn.se2.model.objects.dto.Car;

import java.util.List;

public class SearchControl implements SearchControlInterface {

    private static SearchControlInterface search;

    public SearchControl() {
    }

    public static SearchControl getInstance() {
        if (search == null) {
            search = new SearchControl();
        }
        return (SearchControl) search;
    }

    @Override
    public List<Car> getAllCars() {
        return null;

    }
}
