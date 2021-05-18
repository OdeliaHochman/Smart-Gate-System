package com.example.smartgate;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String mail;
    private int code;


    public User() {
    }

    public User(String name, String mail, int code) {
        this.name = name;
        this.mail = mail;
        this.code = code;

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

    public Integer getCode() {
        return code;
    }


}