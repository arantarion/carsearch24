package org.bonn.se2.model.dao;

import org.bonn.se2.model.objects.dto.Reservation;
import org.bonn.se2.model.objects.dto.User;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.process.control.exceptions.DontUseException;
import org.bonn.se2.process.control.exceptions.InvalidCredentialsException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    public Reservation retrieve(int id) throws DatabaseException, InvalidCredentialsException {
        //language=PostgreSQL
        final String sql =
                "SELECT * FROM \"CarSearch24\".reservation " +
                        "WHERE \"reservationID\" = '" + id + "';";

        List<Reservation> result = execute(sql);
        if (result.size() < 1) {
            throw new InvalidCredentialsException();
        }
        return result.get(0);
    }

    @Override
    public Reservation retrieve(String attribute) throws DatabaseException, InvalidCredentialsException, DontUseException {
        return null;
    }

    @Override
    public List<Reservation> retrieveAll() throws DatabaseException {
        //language=PostgreSQL
        final String sql = "SELECT * FROM \"CarSearch24\".reservation";
        return execute(sql);
    }

    public List<Integer> retrieveReservations(int id) throws DatabaseException, InvalidCredentialsException {
        //language=PostgreSQL
        final String sql = "SELECT * FROM \"CarSearch24\".reservation " +
                "WHERE \"customerID\" = '" + id + "';";

        List<Reservation> result = execute(sql);
        if (result.size() < 1) {
            throw new InvalidCredentialsException();
        }
        List<Integer> res = new ArrayList<Integer>();
        for(int i = 0; i < result.size(); i++){
            res.add(result.get(i).getCarID());
        }
        return res;
    }
    @Override
    public Reservation create(Reservation reservation) throws DatabaseException, SQLException {
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
