package org.bonn.se2.process.control.exceptions;

public class DontUseException extends CarSearch24Exception {

    public DontUseException() {
        super("Please dont use this method. It is not ready or does not make sense in the context");
    }

    public DontUseException(String msg) {
        super(msg);
    }

}
