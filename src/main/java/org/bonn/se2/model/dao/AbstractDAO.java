package org.bonn.se2.model.dao;

import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.services.db.JDBCConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO<T> {

    public static final AbstractDAO dao = null;

    protected JDBCConnection connection = JDBCConnection.getInstance();

    protected AbstractDAO() throws DatabaseException {
    }

    protected Statement getStatement() throws DatabaseException {
        return connection.getStatement();
    }

    protected PreparedStatement getPreparedStatement(String sql) throws DatabaseException {
        return connection.getPreparedStatement(sql);
    }

    protected List<T> execute(String sql) throws DatabaseException {
        List<T> results = new ArrayList<>();
        Statement statement = this.getStatement();
        ResultSet resultSet;

        try {
            resultSet = statement.executeQuery(sql);
        } catch (SQLException ex) {
            throw new DatabaseException("Es gibt ein Problem mit dem ResultSet");
        }

        try {
            while (resultSet.next()) {
                results.add(create(resultSet));
            }
            return results;
        } catch (SQLException ex) {
            throw new DatabaseException("Es gibt ein Problem beim Iterieren über das ResultSet");
        } finally {
            connection.closeConnection();
        }
    }


    protected List<T> executePrepared(String sql, Object... values) throws DatabaseException {
        List<T> results = new ArrayList<>();
        PreparedStatement preparedStatement = this.getPreparedStatement(sql);

        try {
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof String) {

                    preparedStatement.setString(i + 1, (String) values[i]);

                } else if (values[i] instanceof Integer) {

                    preparedStatement.setInt(i + 1, (Integer) values[i]);

                } else if (values[i] instanceof ArrayList) {

                    Object[] items = ((ArrayList) values[i]).toArray();

                    if (items.length > 0 && items[0] instanceof String)
                        preparedStatement.setArray(i + 1, preparedStatement.getConnection().createArrayOf("text", items));
                    else if (items.length > 0)
                        preparedStatement.setArray(i + 1, preparedStatement.getConnection().createArrayOf("integer", items));
                    else preparedStatement.setArray(i + 1, null);

                } else if (values[i] instanceof byte[]) {

                    preparedStatement.setBytes(i + 1, (byte[]) values[i]);

                } else if (values[i] instanceof LocalDate) {

                    preparedStatement.setDate(i + 1, Date.valueOf((LocalDate) values[i]));

                } else if (values[i] instanceof Boolean) {

                    preparedStatement.setBoolean(i + 1, (Boolean) values[i]);

                }
            }

            ResultSet set = preparedStatement.executeQuery();
            while (set.next()) {
                results.add(create(set));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler im Prepared Statement");
        } finally {
            connection.closeConnection();
        }

        return results;
    }

    protected abstract T create(ResultSet resultSet) throws DatabaseException;

}
