package org.bonn.se2.process.control.exceptions;

/**
 * @author Henry Weckermann, Anton Drees
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class DontUseException extends CarSearch24Exception {

    public DontUseException() {
        super("Please dont use this method. It is not ready or does not make sense in the context");
    }

    public DontUseException(String msg) {
        super(msg);
    }

}
