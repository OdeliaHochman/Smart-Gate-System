package com.example.smartgate;

import java.io.Serializable;

public class AuthorizedPerson implements Serializable {

    private String firstName;
    private String lastName;
    private String employeeNumber;
    private String IDNumber;    //int or string?
    private String LPNumber;


    public AuthorizedPerson() {
    }

    public AuthorizedPerson(String firstName, String lastName,String IDNumber, String LPNumber, String employeeNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.IDNumber = IDNumber;
        this.LPNumber = LPNumber;
        this.employeeNumber = employeeNumber;

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

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getIDNumber() {
        return IDNumber;
    }


    public String getLPNumber() {
        return LPNumber;
    }

    public void setLPNumber(String LPNumber) {
        this.LPNumber = LPNumber;
    }
}
