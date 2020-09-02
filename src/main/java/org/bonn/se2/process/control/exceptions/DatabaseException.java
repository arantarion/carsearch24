package org.bonn.se2.process.control.exceptions;

/**
 * @author Henry Weckermann, Anton Drees
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class DatabaseException extends CarSearch24Exception {

    public DatabaseException() {
        super("Es gibt ein Problem mit der Datenbank");
    }

    public DatabaseException(String msg) {
        super(msg);
    }

}
