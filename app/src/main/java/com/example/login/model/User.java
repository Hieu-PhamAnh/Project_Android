package com.example.login.model;

public class User {
    String id, name, username, password, email, permission;
    long phone;


    public User() {
    }

    public User(String id, String name, String username, String password, String email, String permission, long phone) {
        this.id= id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.permission = permission;
        this.phone = phone;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", permission='" + permission + '\'' +
                ", phone=" + phone +
                '}';
    }
}
