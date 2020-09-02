package org.bonn.se2.model.objects.dto;

import java.io.Serializable;

/**
 * @author Henry Weckermann
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class UserAtLogin extends User implements Serializable {

    private final String email;
    private final String password;

    public UserAtLogin(String login, String password) {
        this.email = login;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
