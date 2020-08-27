package org.bonn.se2.model.objects.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Reservation implements Serializable {
    private Integer reservationID;
    private LocalDate resDate;
    private Integer CarID;
    private Integer CustomID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return reservationID.equals(that.reservationID) &&
                Objects.equals(resDate, that.resDate) &&
                Objects.equals(CarID, that.CarID) &&
                Objects.equals(CustomID, that.CustomID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationID, resDate, CarID, CustomID);
    }

    public Reservation(Integer reservationID, LocalDate resDate, Integer carID, Integer customID) {
        this.reservationID = reservationID;
        this.resDate = resDate;
        CarID = carID;
        CustomID = customID;
    }

    public void setReservationID(Integer reservationID) {
        this.reservationID = reservationID;
    }

    public void setResDate(LocalDate resDate) {
        this.resDate = resDate;
    }

    public void setCarID(Integer carID) {
        CarID = carID;
    }

    public void setCustomID(Integer customID) {
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
