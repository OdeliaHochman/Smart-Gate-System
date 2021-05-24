package com.example.smartgate;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String mail;
    private int adminCode;


    public User() {
    }

    public User(String name, String mail, int adminCode) {
        this.name = name;
        this.mail = mail;
        this.adminCode = adminCode;

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

    public Integer getAdminCode() {
        return adminCode;
    }


}