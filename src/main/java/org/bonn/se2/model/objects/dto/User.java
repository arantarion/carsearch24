package org.bonn.se2.model.objects.dto;

import org.bonn.se2.services.util.CryptoFunctions;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class User implements Serializable {

    private Integer userID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate registrationsDatum;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password, Integer id, LocalDate registrationsDatum) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userID = id;
        this.registrationsDatum = registrationsDatum;
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = CryptoFunctions.hash(password);
    }

    public void setPwHash(String password) {
        this.password = password;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public LocalDate getRegistrationsDatum() {
        return registrationsDatum;
    }

    public void setRegistrationsDatum(LocalDate registrationsDatum) {
        this.registrationsDatum = LocalDate.from(registrationsDatum);
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userID, user.userID) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(registrationsDatum, user.registrationsDatum);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
