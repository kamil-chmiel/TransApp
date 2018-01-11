package com.example.kamil.transapp;

public class Task {

    private String orderNumber, items, describtion, deadline;
    private Customer customer;
    private WarehouseWorker warehouseWorker;
    private Driver driver;

    public Task(String orderNumber, String items, String describtion, String deadline, Customer customer, WarehouseWorker worker, Driver driver) {
        this.orderNumber = orderNumber;
        this.items = items;
        this.describtion = describtion;
        this.deadline = deadline;
        this.customer = customer;
        this.warehouseWorker = worker;
        this.driver = driver;
    }

    public void sendToDataBase()
    {
        DatabaseHandler.sendTask(this);
    }


    public String getOrderNumber() {
        return orderNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getItems() {
        return items;
    }

    public String getDescribtion() {
        return describtion;
    }

    public String getDeadline() {
        return deadline;
    }

    public WarehouseWorker getWorker() {
        return warehouseWorker;
    }

    public Driver getDriver() {
        return driver;
    }
}
