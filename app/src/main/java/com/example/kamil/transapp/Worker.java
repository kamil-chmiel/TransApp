package com.example.kamil.transapp;

/**
 * Created by Kamil on 26.11.2017.
 */

public class Worker {

    String pesel, name, surname;
    int salary;

    public Worker()
    {

    }

    public Worker(String pesel, String name, String surname, int salary) {
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.salary = salary;
    }

}
