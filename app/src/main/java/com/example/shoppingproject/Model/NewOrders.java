package com.example.shoppingproject.Model;

public class NewOrders {

    private String OrderID,phone, fullName, phoneRecipient, address, OrderTotalAmount, OrderPayment, OrderStateShipped, OrderPackage,OrderDate,OrderTime;

    public NewOrders() {
    }

    public NewOrders(String orderID, String phone, String fullName, String phoneRecipient, String address, String orderTotalAmount, String orderPayment, String orderStateShipped, String orderPackage, String orderDate, String orderTime) {
        OrderID = orderID;
        this.phone = phone;
        this.fullName = fullName;
        this.phoneRecipient = phoneRecipient;
        this.address = address;
        OrderTotalAmount = orderTotalAmount;
        OrderPayment = orderPayment;
        OrderStateShipped = orderStateShipped;
        OrderPackage = orderPackage;
        OrderDate = orderDate;
        OrderTime = orderTime;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneRecipient() {
        return phoneRecipient;
    }

    public void setPhoneRecipient(String phoneRecipient) {
        this.phoneRecipient = phoneRecipient;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderTotalAmount() {
        return OrderTotalAmount;
    }

    public void setOrderTotalAmount(String orderTotalAmount) {
        OrderTotalAmount = orderTotalAmount;
    }

    public String getOrderPayment() {
        return OrderPayment;
    }

    public void setOrderPayment(String orderPayment) {
        OrderPayment = orderPayment;
    }

    public String getOrderStateShipped() {
        return OrderStateShipped;
    }

    public void setOrderStateShipped(String orderStateShipped) {
        OrderStateShipped = orderStateShipped;
    }

    public String getOrderPackage() {
        return OrderPackage;
    }

    public void setOrderPackage(String orderPackage) {
        OrderPackage = orderPackage;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }
}

