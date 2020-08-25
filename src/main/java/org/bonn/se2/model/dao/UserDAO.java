package org.bonn.se2.model.dao;

import org.bonn.se2.model.objects.dto.User;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.process.control.exceptions.InvalidCredentialsException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class UserDAO extends AbstractDAO<User> implements DAOInterface<User> {

    public UserDAO() throws DatabaseException {
        super();
    }

    @Override
    public User retrieve(int id) throws DatabaseException, InvalidCredentialsException {
        //language=PostgreSQL
        final String sql =
                "SELECT * FROM \"CarSearch24\".\"user\" " +
                        "WHERE \"user\".\"userID\" = '" + id + "';";

        List<User> result = execute(sql);
        if (result.size() < 1) {
            throw new InvalidCredentialsException();
        }
        return result.get(0);
    }

    @Override
    public User retrieve(String attribute) throws DatabaseException, InvalidCredentialsException {
        //language=PostgreSQL
        String sql =
                "SELECT * FROM \"CarSearch24\".user " +
                        "WHERE \"CarSearch24\".user.email = '" + attribute + "' ";

        List<User> result = execute(sql);
        if (result.size() < 1) {
            throw new InvalidCredentialsException();
        }
        return result.get(0);
    }

    @Override
    public List<User> retrieveAll() throws DatabaseException {
        //language=PostgreSQL
        final String sql = "SELECT * FROM \"CarSearch24\".user";
        return execute(sql);
    }

    @Override
    public User create(User user) throws DatabaseException {
        //language=PostgreSQL
        final String insertQuery = "INSERT INTO \"CarSearch24\".user (email, \"firstName\", \"lastName\", password,\"registerDate\") " +
                "VALUES (?, ?, ?, ?, ?) " +
                "RETURNING *";

        List<User> result = executePrepared(insertQuery, user.getEmail(), user.getFirstName(), user.getLastName(), user.getPassword(), LocalDate.now());
        if (result.size() < 1) {
            throw new DatabaseException("create(User user) did not return a DTO");
        }
        return result.get(0);
    }

    @Override
    protected User create(ResultSet resultSet) throws DatabaseException {
        User dto;
        try {
            dto = new User(
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("email"),
                    resultSet.getString("password"));
            dto.setUserID(resultSet.getInt("userID"));
            dto.setRegistrationsDatum(resultSet.getDate("registerDate").toLocalDate());

            return dto;
        } catch (SQLException e) {
            throw new DatabaseException("couldn't create UserDTO from resultSet: " + e.getMessage());
        }
    }

    @Override
    public User update(User user) throws DatabaseException {
        //language=PostgreSQL
        final String updateQuery = "UPDATE \"CarSearch24\".user " +
                "SET (email, password, \"firstName\", \"lastName\") = " +
                "('" + user.getEmail() + "', '" + user.getPassword() + "', '" + user.getFirstName() + "', '" + user.getLastName() + "') " +
                "WHERE \"userID\" = " + user.getUserID() + " " +
                "RETURNING *;";

        List<User> result = execute(updateQuery);
        if (result.size() < 1) {
            throw new DatabaseException("[" + UserDAO.class.toString() + "] updateOne() did not return a DTO");
        }
        return result.get(0);
    }

    @Override
    public User delete(User user) throws DatabaseException {
        //language=PostgreSQL
        final String deleteQuery =
                "DELETE FROM \"CarSearch24\".user " +
                        "WHERE \"userID\" = ? " +
                        "RETURNING *;";

        List<User> result = executePrepared(deleteQuery, user.getUserID());
        if (result.size() < 1) {
            throw new DatabaseException("delete(User user) failed");
        }
        return result.get(0);
    }

    @Override
    public User delete(int id) throws DatabaseException {
        //language=PostgreSQL
        final String deleteQuery =
                "DELETE FROM \"CarSearch24\".user\n" +
                        "WHERE \"userID\" = ?\n" +
                        "RETURNING *;";

        List<User> result = executePrepared(deleteQuery, id);
        if (result.size() < 1) {
            throw new DatabaseException("delete(int ID) failed");
        }
        return result.get(0);
    }

}
