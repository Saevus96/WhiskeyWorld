package com.example.kpchl.whiskeyworld.using_classes;

import java.util.UUID;

public class OrderClass {
    String orderDate;
    String status;
    String payValue;
    String orderId;

    public OrderClass(){

    }

    public OrderClass(String orderDate, String status, String payValue, String orderId) {
        this.orderDate = orderDate;
        this.status = status;
        this.payValue = payValue;
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayValue() {
        return payValue;
    }

    public void setPayValue(String payValue) {
        this.payValue = payValue;
    }
}
