package org.bonn.se2.model.dao;

import org.bonn.se2.model.objects.dto.Salesman;
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

public class SalesmanDAO extends AbstractDAO<Salesman> implements DAOInterface<Salesman> {

    public SalesmanDAO() throws DatabaseException {
    }

    @Override
    public Salesman retrieve(int id) throws DatabaseException {
        //language=PostgreSQL
        final String sql =
                "SELECT * FROM \"CarSearch24\".user " +
                        "JOIN \"CarSearch24\".salesman ON \"user\".\"userID\" = salesman.\"userID\" " +
                        "WHERE salesman.\"userID\" = '" + id + "'" + " OR salesman.\"salesmanID\" = '" + id + "';";

        List<Salesman> result = execute(sql);

        if (result.size() < 1) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                    new Throwable().getStackTrace()[0].getMethodName() + " failed");
            throw new DatabaseException("retrieve(int id) did not return a DTO");
        }
        Logger.getLogger(SalesmanDAO.class.getName()).log(Level.INFO, "Der Salesman mit der ID: " + id + " wurde erfolgreich abgerufen.");
        return result.get(0);
    }

    @Override
    public Salesman retrieve(String attribute) throws DatabaseException {
        //language=PostgreSQL
        final String sql =
                "SELECT * FROM \"CarSearch24\".user " +
                        "JOIN \"CarSearch24\".salesman ON \"user\".\"userID\" = salesman.\"userID\" " +
                        "WHERE email = ? ";

        List<Salesman> result = executePrepared(sql, attribute, attribute);
        if (result.size() < 1) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                    new Throwable().getStackTrace()[0].getMethodName() + " failed");
            throw new DatabaseException("retrieve(String attribute) did not return a DTO");
        }
        Logger.getLogger(SalesmanDAO.class.getName()).log(Level.INFO, "Der Salesman mit der Email: " + attribute + " wurde erfolgreich abgerufen.");
        return result.get(0);
    }


    @Override
    public List<Salesman> retrieveAll() throws DatabaseException {
        //language=PostgreSQL
        String sql =
                "SELECT * FROM \"CarSearch24\".user " +
                        "JOIN \"CarSearch24\".salesman ON \"user\".\"userID\" = salesman.\"userID\"";

        Logger.getLogger(SalesmanDAO.class.getName()).log(Level.INFO, "Alle Verkäufer wurden abgerufen.");
        return execute(sql);
    }

    @Override
    public Salesman create(Salesman salesman) throws DatabaseException, SQLException {
        User user = new UserDAO().create(salesman);

        //language=PostgreSQL
        final String query =
                "INSERT INTO \"CarSearch24\".salesman (\"userID\")\n" +
                        "VALUES (" + user.getUserID() + ") " +
                        "RETURNING *";

        PreparedStatement preparedStatement = this.getPreparedStatement(query);

        ResultSet set = preparedStatement.executeQuery();

        if (set.next()) {
            Salesman salesman1 = new Salesman();
            salesman1.setUserID(set.getInt("userID"));
            salesman1.setSalesmanID(set.getInt("SalesmanID"));

            salesman1.setFirstName(user.getFirstName());
            salesman1.setLastName(user.getLastName());
            salesman1.setEmail(user.getEmail());
            salesman1.setPwHash(user.getPassword());

            Logger.getLogger(SalesmanDAO.class.getName()).log(Level.INFO, "Der Salesman : " + salesman1 + " konnte erfolgreich gespeichert werden.");
            return salesman1;
        } else {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                    new Throwable().getStackTrace()[0].getMethodName() + " failed");
            return null;
        }
    }

    @Override
    protected Salesman create(ResultSet resultSet) throws DatabaseException {
        Salesman dto = new Salesman();

        try {
            dto.setUserID(resultSet.getInt("userID"));
            dto.setSalesmanID(resultSet.getInt("SalesmanID"));

            User user = new UserDAO().retrieve(dto.getUserID());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setPwHash(user.getPassword());


        } catch (SQLException | InvalidCredentialsException e) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                    new Throwable().getStackTrace()[0].getMethodName() + " failed", e);
            throw new DatabaseException("create(ResultSet resultSet) in SalesmanDAO failed");
        }

        Logger.getLogger(SalesmanDAO.class.getName()).log(Level.INFO, "Salesman: " + dto + " wurde erfolgreich gespeichert.");
        return dto;
    }

    @Override
    public Salesman delete(Salesman salesman) throws DatabaseException {
        //language=PostgreSQL
        final String deleteQuery =
                "DELETE FROM \"CarSearch24\".user " +
                        "WHERE \"userID\" = ? " +
                        "RETURNING *;";

        List<Salesman> result = executePrepared(deleteQuery, salesman.getSalesmanID());
        if (result.size() < 1) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                    new Throwable().getStackTrace()[0].getMethodName() + " failed");
            throw new DatabaseException("delete(Salesman Salesman) failed");
        }
        Logger.getLogger(SalesmanDAO.class.getName()).log(Level.INFO, "Salesman: " + salesman + " wurde erfolgreich gelöscht.");
        return result.get(0);
    }

    public Salesman delete(int id) throws DatabaseException {
        //language=PostgreSQL
        final String deleteQuery =
                "DELETE FROM \"CarSearch24\".Salesman " +
                        "WHERE \"salesmanID\" = ? " +
                        "RETURNING *;";

        List<Salesman> result = executePrepared(deleteQuery, id);
        if (result.size() < 1) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                    new Throwable().getStackTrace()[0].getMethodName() + " failed");
            throw new DatabaseException("delete(int ID) failed");
        }
        Logger.getLogger(SalesmanDAO.class.getName()).log(Level.SEVERE, "Salesman mit der ID: " + id + " wurde erfolgreich gelöscht.");
        return result.get(0);
    }

}
