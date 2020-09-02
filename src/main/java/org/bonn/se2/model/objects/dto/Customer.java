package org.bonn.se2.model.objects.dto;

import java.io.Serializable;

/**
 * @author Henry Weckermann
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class Customer extends User implements Serializable {

    private Integer customerID;

    public Customer(User user) {
        super(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
    }

    public Customer() { }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }
}
