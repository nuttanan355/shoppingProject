package com.example.shoppingproject.Model;

public class Users {
    private String phone,password,userName,fullName,image,address,phoneRecipient,postalCode;

    public Users(){

    }

    public Users(String phone, String password, String userName, String fullName, String image, String address, String phoneRecipient, String postalCode) {
        this.phone = phone;
        this.password = password;
        this.userName = userName;
        this.fullName = fullName;
        this.image = image;
        this.address = address;
        this.phoneRecipient = phoneRecipient;
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneRecipient() {
        return phoneRecipient;
    }

    public void setPhoneRecipient(String phoneRecipient) {
        this.phoneRecipient = phoneRecipient;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
