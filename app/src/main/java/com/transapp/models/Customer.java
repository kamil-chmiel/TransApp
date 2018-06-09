package com.transapp.models;


public class Customer {

    private String pesel, name, surname, address;

    public Customer(String pesel, String name, String surname, String address) {
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.address = address;
    }

    public String getPesel() {
        return pesel;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }
}
