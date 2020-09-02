package org.bonn.se2.model.objects.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Anton Drees
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class Reservation implements Serializable {
    private Integer reservationID;
    private LocalDate resDate;
    private Integer CarID;
    private Integer CustomID;

    public Reservation(Integer reservationID, LocalDate resDate, Integer carID, Integer customID) {
        this.reservationID = reservationID;
        this.resDate = resDate;
        this.CarID = carID;
        this.CustomID = customID;
    }

    public Reservation(Integer carID, Integer customID) {
        this.CarID = carID;
        this.CustomID = customID;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationID=" + reservationID +
                ", resDate=" + resDate +
                ", CarID=" + CarID +
                ", CustomID=" + CustomID +
                '}';
    }

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

    public Integer getReservationID() {
        return reservationID;
    }

    public void setReservationID(Integer reservationID) {
        this.reservationID = reservationID;
    }

    public LocalDate getResDate() {
        return resDate;
    }

    public void setResDate(LocalDate resDate) {
        this.resDate = resDate;
    }

    public Integer getCarID() {
        return CarID;
    }

    public void setCarID(Integer carID) {
        CarID = carID;
    }

    public Integer getCustomID() {
        return CustomID;
    }

    public void setCustomID(Integer customID) {
        CustomID = customID;
    }
}
