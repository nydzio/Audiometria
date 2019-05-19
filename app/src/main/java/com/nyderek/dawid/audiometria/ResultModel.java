package com.nyderek.dawid.audiometria;


public class ResultModel {

    private Integer id;
    private String firstName;
    private String lastName;
    private String date;
    private byte[] audiogram;

    public ResultModel(String firstName, String lastName, byte[] audiogram) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.audiogram = audiogram;
    }

    public ResultModel(Integer id, String firstName, String lastName, String date, byte[] audiogram) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.audiogram = audiogram;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public byte[] getAudiogram() {
        return audiogram;
    }

    public void setAudiogram(byte[] audiogram) {
        this.audiogram = audiogram;
    }
}
