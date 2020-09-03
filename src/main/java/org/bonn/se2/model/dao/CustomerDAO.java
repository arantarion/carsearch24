package org.bonn.se2.model.dao;

import org.bonn.se2.model.objects.dto.Customer;
import org.bonn.se2.model.objects.dto.User;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.process.control.exceptions.InvalidCredentialsException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Henry Weckermann, Anton Drees
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class CustomerDAO extends AbstractDAO<Customer> implements DAOInterface<Customer> {

    public CustomerDAO() throws DatabaseException {
    }

    @Override
    public Customer retrieve(int id) throws DatabaseException {
        //language=PostgreSQL
        final String sql =
                "SELECT * FROM \"CarSearch24\".user " +
                        "JOIN \"CarSearch24\".customer ON \"user\".\"userID\" = customer.\"userID\" " +
                        "WHERE customer.\"userID\" = '" + id + "'" + " OR customer.\"customerID\" = '" + id + "';";

        List<Customer> result = execute(sql);

        if (result.size() < 1) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, "retrieve(int id) did not return a DTO.");
            throw new DatabaseException("retrieve(int id) did not return a DTO");
        }
        Logger.getLogger(CustomerDAO.class.getName()).log(Level.INFO, "Der Customer mit der ID: " + id + " wurde erfolgreich abgerufen.");
        return result.get(0);
    }

    @Override
    public Customer retrieve(String attribute) throws DatabaseException {
        //language=PostgreSQL
        final String sql =
                "SELECT * FROM \"CarSearch24\".user " +
                        "JOIN \"CarSearch24\".customer ON \"user\".\"userID\" = customer.\"userID\" " +
                        "WHERE email = ? ";

        List<Customer> result = executePrepared(sql, attribute, attribute);
        if (result.size() < 1) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, "retrieve(String attribute) did not return a DTO.");
            throw new DatabaseException("retrieve(String attribute) did not return a DTO");
        }
        Logger.getLogger(CustomerDAO.class.getName()).log(Level.INFO, "Der Customer mit der Email: " + attribute + " wurde erfolgreich abgerufen.");
        return result.get(0);
    }


    @Override
    public List<Customer> retrieveAll() throws DatabaseException {
        //language=PostgreSQL
        String sql =
                "SELECT * FROM \"CarSearch24\".user " +
                        "JOIN \"CarSearch24\".customer ON \"user\".\"userID\" = customer.\"userID\"";

        Logger.getLogger(CustomerDAO.class.getName()).log(Level.INFO, "Alle Customers wurden abgerufen.");
        return execute(sql);
    }

    @Override
    public Customer create(Customer customer) throws DatabaseException, SQLException {
        User user = new UserDAO().create(customer);

        //language=PostgreSQL
        final String query =
                "INSERT INTO \"CarSearch24\".customer (\"userID\")\n" +
                        "VALUES (" + user.getUserID() + ") " +
                        "RETURNING *";

        PreparedStatement preparedStatement = this.getPreparedStatement(query);

        ResultSet set = preparedStatement.executeQuery();

        if (set.next()) {
            Customer customer1 = new Customer();
            customer1.setUserID(set.getInt("userID"));
            customer1.setCustomerID(set.getInt("CustomerID"));

            customer1.setFirstName(user.getFirstName());
            customer1.setLastName(user.getLastName());
            customer1.setEmail(user.getEmail());
            customer1.setPwHash(user.getPassword());

            Logger.getLogger(CustomerDAO.class.getName()).log(Level.INFO, "Der Customer : " + customer1 + " konnte erfolgreich gespeichert werden.");
            return customer1;
        } else {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.INFO, "Der Customer : " + customer + " konnte nicht erfolgreich gespeichert werden.");
            return null;
        }
    }

    @Override
    protected Customer create(ResultSet resultSet) throws DatabaseException {
        Customer dto = new Customer();

        try {
            dto.setUserID(resultSet.getInt("userID"));
            dto.setCustomerID(resultSet.getInt("CustomerID"));

            User user = new UserDAO().retrieve(dto.getUserID());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setPwHash(user.getPassword());


        } catch (SQLException | InvalidCredentialsException e) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                    new Throwable().getStackTrace()[0].getMethodName() + " failed", e);
            throw new DatabaseException("create(ResultSet resultSet) in CustomerDAO failed");
        }

        Logger.getLogger(CustomerDAO.class.getName()).log(Level.INFO, "Customer: " + dto + " wurde erfolgreich gespeichert.");
        return dto;
    }

    @Override
    public Customer delete(Customer customer) throws DatabaseException {
        //language=PostgreSQL
        final String deleteQuery =
                "DELETE FROM \"CarSearch24\".user " +
                        "WHERE \"userID\" = ? " +
                        "RETURNING *;";

        List<Customer> result = executePrepared(deleteQuery, customer.getCustomerID());
        if (result.size() < 1) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                    new Throwable().getStackTrace()[0].getMethodName() + " failed");
            throw new DatabaseException("delete(Customer Customer) failed");
        }
        Logger.getLogger(CustomerDAO.class.getName()).log(Level.INFO, "Customer: " + customer + " wurde erfolgreich gelöscht.");
        return result.get(0);
    }

    public Customer delete(int id) throws DatabaseException {
        //language=PostgreSQL
        final String deleteQuery =
                "DELETE FROM \"CarSearch24\".Customer " +
                        "WHERE \"customerID\" = ? " +
                        "RETURNING *;";

        List<Customer> result = executePrepared(deleteQuery, id);
        if (result.size() < 1) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                    new Throwable().getStackTrace()[0].getMethodName() + " failed");
            throw new DatabaseException("delete(int ID) failed");
        }
        Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, "Customer mit der ID: " + id + " wurde erfolgreich gelöscht.");
        return result.get(0);
    }

}
