package com.example.kamil.transapp;


public class WarehouseWorker {

    private String pesel, name, surname;

    public WarehouseWorker(String pesel, String name, String surname) {
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
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

}
