package org.bonn.se2.process.control.exceptions;

/**
 * @author Henry Weckermann, Anton Drees
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class InvalidCredentialsException extends CarSearch24Exception {

    public InvalidCredentialsException() {
        super("Die angegebenen Login-Daten sind nicht korrekt. Versuchen Sie es erneut.");
    }

    public InvalidCredentialsException(String msg) {
        super(msg);
    }

}
