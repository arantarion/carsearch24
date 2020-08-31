package org.bonn.se2.process.control;

import org.bonn.se2.model.objects.dto.Car;

import java.util.List;

public interface SearchControlInterface {

    static SearchControlInterface getInstance() {
        return null;
    }

    List<Car> getAllCars();

}
