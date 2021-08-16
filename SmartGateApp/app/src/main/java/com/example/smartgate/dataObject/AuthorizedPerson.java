package com.example.smartgate.dataObject;

import android.widget.VideoView;

import java.io.Serializable;

public class AuthorizedPerson implements Serializable {

    private String firstName;
    private String lastName;
    private String employeeNumber;
    private String IDNumber;
    private String LPNumber;
    private String urlImage;
    private ModelVideo modelVideo;




    public AuthorizedPerson() {
    }

    public AuthorizedPerson(String firstName, String lastName,String IDNumber, String LPNumber, String employeeNumber,String urlImage,ModelVideo modelVideo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.IDNumber = IDNumber;
        this.LPNumber = LPNumber;
        this.employeeNumber = employeeNumber;
        this.urlImage = urlImage;
        this.modelVideo = modelVideo;

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

    public String getUrlImage() { return urlImage; }

    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }

    public ModelVideo getModelVideo() { return modelVideo; }

    public void setModelView(ModelVideo modelVideo ) { this.modelVideo =modelVideo; }

    public void setIDNumber(String IDNumber) {  this.IDNumber = IDNumber;
    }
}
