package com.abhiandroid.adventureindia.Model;

public class User {

    private String username, email, gender;
    private Integer id;

    public User(String username, String email, String gender, Integer id) {
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.id =id;
    }
    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }
}
