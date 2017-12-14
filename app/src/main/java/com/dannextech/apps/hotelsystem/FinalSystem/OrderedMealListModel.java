package com.dannextech.apps.hotelsystem.FinalSystem;

/**
 * Created by amoh on 10/1/2017.
 */

public class OrderedMealListModel {
    private String tableName,timeOrdered,paymentStatus,serviceStatus,totalPrice;

    public OrderedMealListModel(String tableName, String timeOrdered, String paymentStatus, String serviceStatus, String totalPrice) {
        this.tableName = tableName;
        this.timeOrdered = timeOrdered;
        this.paymentStatus = paymentStatus;
        this.serviceStatus = serviceStatus;
        this.totalPrice = totalPrice;

    }

    public OrderedMealListModel(){

    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTimeOrdered() {
        return timeOrdered;
    }

    public void setTimeOrdered(String timeOrdered) {
        this.timeOrdered = timeOrdered;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
