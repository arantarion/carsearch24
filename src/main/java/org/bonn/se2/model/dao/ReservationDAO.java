package org.bonn.se2.model.dao;

import org.bonn.se2.model.objects.dto.Reservation;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.process.control.exceptions.DontUseException;
import org.bonn.se2.process.control.exceptions.InvalidCredentialsException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReservationDAO extends AbstractDAO<Reservation> implements DAOInterface<Reservation>{

    public ReservationDAO() throws DatabaseException {
    }

    @Override
    protected Reservation create(ResultSet resultSet) throws DatabaseException {
        return null;
    }

    @Override
    public Reservation retrieve(int id) throws DatabaseException, InvalidCredentialsException {
        return null;
    }

    @Override
    public Reservation retrieve(String attribute) throws DatabaseException, InvalidCredentialsException, DontUseException {
        return null;
    }

    @Override
    public List<Reservation> retrieveAll() throws DatabaseException {
        return null;
    }

    @Override
    public Reservation create(Reservation user) throws DatabaseException, SQLException {
        return null;
    }

    @Override
    public Reservation delete(Reservation user) throws DatabaseException {
        return null;
    }

    @Override
    public Reservation delete(int id) throws DatabaseException {
        return null;
    }
}
