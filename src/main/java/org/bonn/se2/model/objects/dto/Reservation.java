package org.bonn.se2.model.objects.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class Reservation implements Serializable {
    private Integer reservationID;
    private LocalDate resDate;
    private Integer CarID;
    private Integer CustomID;

    public Reservation(Integer reservationID, LocalDate resDate, Integer carID, Integer customID) {
        this.reservationID = reservationID;
        this.resDate = resDate;
        CarID = carID;
        CustomID = customID;
    }

    public Integer getReservationID() {
        return reservationID;
    }

    public LocalDate getResDate() {
        return resDate;
    }

    public Integer getCarID() {
        return CarID;
    }

    public Integer getCustomID() {
        return CustomID;
    }
}
