package org.bonn.se2.model.dao;

import org.bonn.se2.model.objects.dto.Car;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.process.control.exceptions.DontUseException;
import org.bonn.se2.process.control.exceptions.InvalidCredentialsException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarDAO extends AbstractDAO<Car> implements DAOInterface<Car> {

    public CarDAO() throws DatabaseException {
    }

    @Override
    public Car retrieve(int carID) throws DatabaseException, InvalidCredentialsException {
        //language=PostgreSQL
        final String sql =
                "SELECT * FROM \"CarSearch24\".Car " +
                        "WHERE car.\"carID\" = '" + carID + "';";

        List<Car> result = execute(sql);
        if (result.size() < 1) {
            throw new InvalidCredentialsException();
        }
        String loggerMsg = "Das Cars-Objekt mit der carID: " + carID + " wurde abgerufen.";
        Logger.getLogger(CarDAO.class.getName()).log(Level.INFO, loggerMsg);
        return result.get(0);
    }

    @Override
    public Car retrieve(String attribute) throws DontUseException {
        throw new DontUseException();
    }


    public List<Car> retrieveCar(String attribute) throws DatabaseException, SQLException {
        Statement statement = this.getStatement();
        ResultSet resultSet;
        attribute = attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
        //language=PostgreSQL
        final String insert = "SELECT * " +
                "FROM \"CarSearch24\".car " +
                "WHERE model LIKE '%" + attribute + "%' " +
                "OR brand LIKE '%" + attribute + "%' " +
                "OR buildyear LIKE '%" + attribute + "%' " +
                "OR color LIKE '%" + attribute + "%' " +
                "OR price LIKE '%" + attribute + "%' ";

        resultSet = statement.executeQuery(insert);
        List<Car> liste = new ArrayList<>();
        Car dto = null;

        try {
            while (resultSet.next()) {
                dto = new Car();
                dto.setModel(resultSet.getString("model"));
                dto.setBrand(resultSet.getString("brand"));
                dto.setDescription(resultSet.getString("description"));
                dto.setCarID(resultSet.getInt("carID"));
                dto.setPrice(resultSet.getString("price"));
                dto.setColor(resultSet.getString("color"));
                dto.setSalesmanID(resultSet.getInt("salesmanID"));
                dto.setCreationDate(new java.sql.Date(resultSet.getDate("creationDate").getTime()).toLocalDate()); //creationDate
                dto.setBuildYear(Integer.parseInt(resultSet.getString("buildyear")));
                liste.add(dto);
            }
            Logger.getLogger(CarDAO.class.getName()).log(Level.INFO, "Alle offer mit Attribut: " + attribute + " wurden abgerufen");
        } catch (SQLException e) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, "retrieveCar(String attribute) in CarDAO failed", e);
        }
        return liste;
    }

    public List<Car> retrieveCarBySalesman(int salesmanID) throws DatabaseException, SQLException {
        Statement statement = this.getStatement();
        ResultSet resultSet;

        //language=PostgreSQL
        final String insert = "SELECT * " +
                "FROM \"CarSearch24\".car " +
                "WHERE \"salesmanID\" = " + salesmanID;

        resultSet = statement.executeQuery(insert);
        List<Car> liste = new ArrayList<>();
        Car dto = null;

        try {
            while (resultSet.next()) {
                dto = new Car();
                dto.setModel(resultSet.getString("model"));
                dto.setBrand(resultSet.getString("brand"));
                dto.setDescription(resultSet.getString("description"));
                dto.setCarID(resultSet.getInt("carID"));
                dto.setPrice(resultSet.getString("price"));
                dto.setColor(resultSet.getString("color"));
                dto.setSalesmanID(resultSet.getInt("salesmanID"));
                dto.setCreationDate(new java.sql.Date(resultSet.getDate("creationDate").getTime()).toLocalDate()); //creationDate
                dto.setBuildYear(Integer.parseInt(resultSet.getString("buildyear")));
                liste.add(dto);
            }
            Logger.getLogger(CarDAO.class.getName()).log(Level.INFO, "Alle Autos von Salesman mit ID: " + salesmanID + " wurden abgerufen");
        } catch (SQLException e) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, "retrieveCarBySalesman(int salesmanID) in JobOfferDAO failed", e);
        }
        return liste;
    }

    @Override
    public List<Car> retrieveAll() throws DatabaseException {
        final String sql = "SELECT * FROM \"CarSearch24\".car";
        Logger.getLogger(CarDAO.class.getName()).log(Level.INFO, "Alle Autos wurden abgerufen.");
        return execute(sql);
    }

    @Override
    public Car create(Car car) throws DatabaseException, SQLException {
        //language=PostgreSQL
        final String insertQuery2 = "INSERT INTO \"CarSearch24\".car ( brand, model, buildyear, color, price, description, \"creationDate\", \"salesmanID\") " +
                "VALUES ('" + car.getBrand() +
                "', '" + car.getModel() +
                "', '" + car.getBuildYear() +
                "', '" + car.getColor() +
                "', '" + car.getPrice() +
                "', '" + car.getDescription() +
                "', '" + LocalDate.now() +
                "', '" + car.getSalesmanID() +
                "') " +
                "RETURNING *";
        PreparedStatement pst = this.getPreparedStatement(insertQuery2);
        ResultSet resultSet = pst.executeQuery();
        if (resultSet.next()) {
            Car offer = new Car();
            offer.setCarID(resultSet.getInt("carID"));
            offer.setBrand(resultSet.getString("brand"));
            offer.setModel(resultSet.getString("model"));
            offer.setBuildYear(resultSet.getInt("buildyear"));
            offer.setColor(resultSet.getString("color"));
            offer.setPrice(resultSet.getString("price"));
            offer.setDescription(resultSet.getString("description"));
            offer.setCreationDate(new java.sql.Date(resultSet.getDate("creationDate").getTime()).toLocalDate());
            offer.setSalesmanID(resultSet.getInt("salesmanID"));
            Logger.getLogger(CarDAO.class.getName()).log(Level.INFO, "Cars-Objekt: " + offer + " wurde erfolgreich gespeichert.");
            return offer;
        } else {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, "Cars-Objekt: " + car + " konnte nicht richtig gespeichert werden.");
            return null;
        }
    }

    @Override
    protected Car create(ResultSet resultSet) {
        Car dto = new Car();
        try {
            dto.setCarID(resultSet.getInt("carID"));
            dto.setBrand(resultSet.getString("brand"));
            dto.setModel(resultSet.getString("model"));
            dto.setBuildYear(resultSet.getInt("buildyear"));
            dto.setColor(resultSet.getString("color"));
            dto.setPrice(resultSet.getString("price"));
            dto.setDescription(resultSet.getString("description"));
            dto.setCreationDate(new java.sql.Date(resultSet.getDate("creationDate").getTime()).toLocalDate());
            dto.setSalesmanID(resultSet.getInt("salesmanID"));
            Logger.getLogger(CarDAO.class.getName()).log(Level.INFO, "Cars-Objekt: " + dto + " wurde erfolgreich gespeichert.");
        } catch (SQLException e) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, "create(ResultSet resultSet) in CarsDAO failed", e);
        }
        return dto;
    }

    @Override
    public Car delete(Car car) throws DatabaseException {

        //language=PostgreSQL
        final String deleteQuery = " DELETE FROM \"CarSearch24\".car WHERE \"carID\" = " + car.getCarID() + " RETURNING * ";

        List<Car> result = execute(deleteQuery);

        if (result.size() < 1) {
            throw new DatabaseException("delete(Car car) failed");
        }
        return result.get(0);
    }

    public Car delete(int carID) throws DatabaseException {
        //language=PostgreSQL
        final String deleteQuery = "DELETE FROM \"CarSearch24\".car WHERE \"carID\" = " + carID + " RETURNING *";
        List<Car> result = execute(deleteQuery);
        if (result.size() < 1) {
            throw new DatabaseException("delete(int carID) failed");
        }
        return result.get(0);
    }

}
