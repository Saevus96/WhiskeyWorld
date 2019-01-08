package com.example.kpchl.whiskeyworld.using_classes;

public class SingleAddressInfo {

    private String country;
    private String phoneNumber;
    private String city;
    private String address;
    private String postCode;
    private String name;
    private String surname;
    private String email;
    private String key;
    private String countryCode;

    public SingleAddressInfo(){

    }
    public SingleAddressInfo(String country, String phoneNumber, String city, String address, String postCode, String name, String surname, String email, String key, String countryCode) {
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.address = address;
        this.postCode = postCode;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.key = key;
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
