package org.bonn.se2.model.dao;

import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.process.control.exceptions.DontUseException;
import org.bonn.se2.process.control.exceptions.InvalidCredentialsException;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Henry Weckermann, Anton Drees
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public interface DAOInterface<T> {
    T retrieve(int id) throws DatabaseException, InvalidCredentialsException;

    T retrieve(String attribute) throws DatabaseException, InvalidCredentialsException, DontUseException;

    List<T> retrieveAll() throws DatabaseException;

    T create(T user) throws DatabaseException, SQLException;

    T delete(T user) throws DatabaseException;

    T delete(int id) throws DatabaseException;
}
