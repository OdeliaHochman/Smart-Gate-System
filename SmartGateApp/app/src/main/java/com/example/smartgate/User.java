package com.example.smartgate;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String mail;
    private String adminCode;
    private String password;


    public User() {
    }

    public User(String name, String mail, String adminCode, String password) {
        this.name = name;
        this.mail = mail;
        this.adminCode = adminCode;
        this.password = password;

    }


    public void setName(String name) {
        this.name = name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }



    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public String getPassword() {
        return password;
    }
}