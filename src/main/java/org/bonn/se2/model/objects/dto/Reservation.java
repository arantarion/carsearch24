package org.bonn.se2.model.objects.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class Reservation implements Serializable {
    private Integer reservationID;
    private LocalDate resDate;
    private Integer CarID;
    private Integer CustomID;
}
