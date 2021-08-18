package com.example.ad_project_kampung_unite.entities;

import java.util.List;

public class UserDetail extends UserLogin{
    private int id;
    private String firstName;
    private String lastName;
    private String role;
    private String phoneNumber;
    private String homeAddress;
    private List<GroceryList> grocerylists;

    public UserDetail(String username, String password, int id, String firstName, String lastName, String role, String phoneNumber, String homeAddress) {
        super(username,password);
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.homeAddress = homeAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public List<GroceryList> getGrocerylists() {
        return grocerylists;
    }

    public void setGrocerylists(List<GroceryList> grocerylists) {
        this.grocerylists = grocerylists;
    }
}
