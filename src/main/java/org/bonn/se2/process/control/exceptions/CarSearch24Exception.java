package org.bonn.se2.process.control.exceptions;

public class CarSearch24Exception extends Exception {

    private String reason;

    public CarSearch24Exception(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
