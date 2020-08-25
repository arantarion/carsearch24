package org.bonn.se2.model.objects.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Car implements Serializable {

    private Integer carID;
    private Integer buildYear;
    private String price;
    private String brand;
    private String description;
    private String color;
    private String model;
    private LocalDate creationDate;

    public Car() {
    }

    public Car(Integer carID, Integer buildYear, String price, String brand, String description, String color, String model, LocalDate creationDate) {
        this.carID = carID;
        this.buildYear = buildYear;
        this.price = price;
        this.brand = brand;
        this.description = description;
        this.color = color;
        this.model = model;
        this.creationDate = creationDate;
    }

    public Integer getCarID() {
        return carID;
    }

    public void setCarID(Integer carID) {
        this.carID = carID;
    }

    public Integer getBuildYear() {
        return buildYear;
    }

    public void setBuildYear(Integer buildYear) {
        this.buildYear = buildYear;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = LocalDate.from(creationDate);
    }

    @Override
    public String toString() {
        return "Car{" +
                "carID=" + carID +
                ", build year=" + buildYear +
                ", price=" + price +
                ", brand='" + brand + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                ", model='" + model + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(getCarID(), car.getCarID()) &&
                Objects.equals(getBuildYear(), car.getBuildYear()) &&
                Objects.equals(getPrice(), car.getPrice()) &&
                Objects.equals(getBrand(), car.getBrand()) &&
                Objects.equals(getDescription(), car.getDescription()) &&
                Objects.equals(getColor(), car.getColor()) &&
                Objects.equals(getModel(), car.getModel()) &&
                Objects.equals(getCreationDate(), car.getCreationDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCarID(), getBuildYear(), getPrice(), getBrand(), getDescription(), getColor(), getModel(), getCreationDate());
    }
}
