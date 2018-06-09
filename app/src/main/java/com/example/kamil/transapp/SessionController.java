package com.example.kamil.transapp;


import java.util.ArrayList;

public class SessionController
{
    private static String accountType;
    private static String peselNumber;
    private static ArrayList<Task> tasks = new ArrayList<>();

    public SessionController(String accountType, String peselNumber) {
        this.accountType = accountType;
        this.peselNumber = peselNumber;
    }

    public static String getAccountType() {
        return accountType;
    }

    public static void setAccountType(String accountType) {
        SessionController.accountType = accountType;
    }

    public static String getPeselNumber() {
        return peselNumber;
    }

    public static void setPeselNumber(String peselNumber) {
        SessionController.peselNumber = peselNumber;
    }

    public static ArrayList<Task> getTasks() {
        return tasks;
    }

    public static void addTask(Task task) {
        SessionController.tasks.add(task);
    }

}
