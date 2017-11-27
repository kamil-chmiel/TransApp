package com.example.kamil.transapp;

public class Order {

    private static Order instance = null;
    public static Order getInstance()
    {
        if(instance == null)
            instance = new Order();
        return instance;
    }

    private Order(){

    }

    int orderNumber;
    private String describtion;
    private String orderDate;
    private String deadline;

    @Override
    public String toString() {
        return "Order" +
                "#" + orderNumber +
                ", orderDate='" + orderDate ;
    }

}
