package org.bonn.se2.model.dao;

import org.bonn.se2.model.objects.dto.Reservation;
import org.bonn.se2.process.control.exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Anton Drees
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class ReservationDAO extends AbstractDAO<Reservation> implements DAOInterface<Reservation> {

    public ReservationDAO() throws DatabaseException {
    }

    @Override
    protected Reservation create(ResultSet resultSet) throws DatabaseException {
        Reservation dto;
        try {
            dto = new Reservation(
                    resultSet.getInt("carID"),
                    resultSet.getInt("customerID"));
            dto.setReservationID(resultSet.getInt("reservationID"));
            dto.setResDate(resultSet.getDate("resDate").toLocalDate());

            return dto;
        } catch (SQLException e) {
            throw new DatabaseException("couldn't create ReservationDTO from resultSet: " + e.getMessage());
        }
    }

    @Override
    public Reservation retrieve(int id) throws DatabaseException {
        //language=PostgreSQL
        final String sql =
                "SELECT * FROM \"CarSearch24\".reservation " +
                        "WHERE \"reservationID\" = '" + id + "';";

        List<Reservation> result = execute(sql);
        if (result.size() < 1) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE,
                    new Throwable().getStackTrace()[0].getMethodName() + " failed");
        }
        return result.get(0);
    }

    @Override
    public Reservation retrieve(String attribute) {
        return null;
    }

    @Override
    public List<Reservation> retrieveAll() throws DatabaseException {
        //language=PostgreSQL
        final String sql = "SELECT * FROM \"CarSearch24\".reservation";
        return execute(sql);
    }

    public List<Integer> retrieveReservationsByCustomerID(int id) throws DatabaseException {
        //language=PostgreSQL
        final String sql = "SELECT * FROM \"CarSearch24\".reservation " +
                "WHERE \"customerID\" = '" + id + "';";

        List<Reservation> result = execute(sql);
        if (result.size() < 1) {
            return null;
        }

        return result.stream().map(Reservation::getCarID).distinct().collect(Collectors.toList());
    }


    @Override
    public Reservation create(Reservation reservation) throws DatabaseException {
        //language=PostgreSQL
        final String insertQuery = "INSERT INTO \"CarSearch24\".reservation (\"resDate\", \"carID\", \"customerID\") " +
                "VALUES (?, ?, ?) " +
                "RETURNING *";

        List<Reservation> result = executePrepared(insertQuery, LocalDate.now(), reservation.getCarID(), reservation.getCustomID());
        if (result.size() < 1) {
            throw new DatabaseException("create(Reservation reservation) did not return a DTO");
        }
        return result.get(0);
    }

    @Override
    public Reservation delete(Reservation reservation) throws DatabaseException {
        //language=PostgreSQL
        final String deleteQuery =
                "DELETE FROM \"CarSearch24\".reservation " +
                        "WHERE \"reservationID\" = ? " +
                        "RETURNING *;";

        List<Reservation> result = executePrepared(deleteQuery, reservation.getReservationID());
        if (result.size() < 1) {
            throw new DatabaseException("delete(Reservation reservation) failed");
        }
        return result.get(0);
    }

    public Reservation deleteByCarID(int customerID, int carID) throws DatabaseException {
        //language=PostgreSQL
        final String deleteQuery =
                "DELETE FROM \"CarSearch24\".reservation\n" +
                        "WHERE \"customerID\" = ?\n" +
                        "AND \"carID\" = ?\n" +
                        "RETURNING *;";

        List<Reservation> result = executePrepared(deleteQuery, customerID, carID);
        if (result.size() < 1) {
            throw new DatabaseException("deleteByCarID(int customerID, int carID) failed");
        }
        return result.get(0);
    }

    @Override
    public Reservation delete(int id) throws DatabaseException {
        //language=PostgreSQL
        final String deleteQuery =
                "DELETE FROM \"CarSearch24\".reservation\n" +
                        "WHERE \"reservationID\" = ?\n" +
                        "RETURNING *;";

        List<Reservation> result = executePrepared(deleteQuery, id);
        if (result.size() < 1) {
            throw new DatabaseException("delete(int ID) failed");
        }
        return result.get(0);
    }
}
