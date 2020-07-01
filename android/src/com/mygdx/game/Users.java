package com.mygdx.game;

public class Users {

    public String name;
    public String status;
    public String role;

    public Users() {
    }

    public Users(String name, String status, String role) {
        this.name = name;

        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

