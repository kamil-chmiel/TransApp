package com.example.kamil.transapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * Created by Kamil on 26.11.2017.
 */

public class WarehouseworkerActivity extends LoginActivity {

    ListView listView;
    ArrayAdapter<String> adapter;
    String[] orders = {
            "Order #120001 24-11-2017 11:31:32",
            "Order #120249 24-11-2017 9:12:11",
            "Order #127233 25-11-2017 16:25:42",
            "Order #124641 26-11-2017 10:46:01",
            "Order #127820 27-11-2017 8:54:55"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_worker);

        listView = (ListView) findViewById(R.id.tasks);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,orders);
        listView.setAdapter(adapter);
    }



}
