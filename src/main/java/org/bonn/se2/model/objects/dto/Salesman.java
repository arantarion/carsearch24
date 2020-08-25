package org.bonn.se2.model.objects.dto;

import java.io.Serializable;

public class Salesman extends User implements Serializable {

    private Integer salesmanID;

    public Salesman(User user) {
        super(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
    }

    public Integer getSalesmanID() {
        return salesmanID;
    }

    public void setSalesmanID(Integer salesmanID) {
        this.salesmanID = salesmanID;
    }
}
