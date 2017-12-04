package com.example.kamil.transapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Created by Kamil on 26.11.2017.
 */

public class ManagerActivity extends LoginActivity {

    ListView listView;
    ArrayAdapter<String> adapter;
    String Data;
    TextView UsernameToChange;
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
        setContentView(R.layout.activity_manager);
        UsernameToChange = (TextView) findViewById(R.id.show_manager_name);
        listView = (ListView) findViewById(R.id.tasks);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,orders);
        listView.setAdapter(adapter);
    }

    public void OnAddUser(View view){
            startActivity(new Intent(ManagerActivity.this,AddUser.class));
    }

    public void OnRemoveUser(View view){
        startActivity(new Intent(ManagerActivity.this,RemoveUser.class));
    }

    public void OnTestButton(View view){
        GetDataFromDatabase getData = new GetDataFromDatabase(this);
        getData.execute("kierowca", "Imie","Login","kadamik");
        Data = getData.getResult();
        //Log.d("tag",Data);
        UsernameToChange.setText(Data);

    }


}
