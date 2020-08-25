package org.bonn.se2.process.control.proxy;

import org.bonn.se2.model.objects.dto.Car;
import org.bonn.se2.process.control.exceptions.DatabaseException;

import java.time.LocalDate;
import java.util.List;

public interface SearchInterface {

    void search(String s) throws DatabaseException;

    List<Car> filterCars(String searchTerm) throws DatabaseException;

    List<Car> filterResults(String brand, String model, String color) throws DatabaseException;

    List<Car> liveFilterCombobox(String searchTerm) throws DatabaseException;

}
