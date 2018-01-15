package com.example.kamil.transapp;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Adams on 2018-01-15.
 */




public class OrderHistory extends ManagerMenu {

    ListView orderHistory;
    ArrayAdapter<String> adapter;
    ArrayList<String> orders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history);

        orderHistory = findViewById(R.id.order_history_list);
        fillOrderHistory();


    }

    private void fillOrderHistory() {
        orders = DatabaseHandler.getDoneTask();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,orders);
        orderHistory.setAdapter(adapter);
    }

}
