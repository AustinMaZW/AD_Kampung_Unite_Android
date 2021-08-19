package com.example.ad_project_kampung_unite.model;

import lombok.Getter;
import lombok.Setter;


public class UserDetail {

    private Integer id;
    private String username;
    private String password;
    private String authentication;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public UserDetail(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
