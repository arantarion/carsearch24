package org.bonn.se2.model.objects.dto;

import java.io.Serializable;

/**
 * @author Henry Weckermann
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class Salesman extends User implements Serializable {

    private Integer salesmanID;

    public Salesman(User user) {
        super(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
    }

    public Salesman() {
    }

    public Integer getSalesmanID() {
        return salesmanID;
    }

    public void setSalesmanID(Integer salesmanID) {
        this.salesmanID = salesmanID;
    }
}
