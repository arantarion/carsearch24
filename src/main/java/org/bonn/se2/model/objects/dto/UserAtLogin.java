package org.bonn.se2.model.objects.dto;

import java.io.Serializable;

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
