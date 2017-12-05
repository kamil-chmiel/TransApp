package com.example.kamil.transapp;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Created by Kamil on 26.11.2017.
 */


public class ManagerActivity extends LoginActivity {

    ListView listView;
    ArrayAdapter<String> adapter;
    TextView NameToChange, SurnameToChange;
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

        Bundle b = getIntent().getExtras();
        String login = b.getString("login");

        NameToChange = (TextView) findViewById(R.id.show_manager_name);
        SurnameToChange = (TextView) findViewById(R.id.show_manager_surname);

        listView = (ListView) findViewById(R.id.tasks);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,orders);
        listView.setAdapter(adapter);


        GetDataFromDatabase setName = new GetDataFromDatabase(this, new AsyncResponse(){
            @Override
            public void returnResult(String Name) {

                NameToChange.setText(Name);

            }

        });

        GetDataFromDatabase setSurname = new GetDataFromDatabase(this, new AsyncResponse(){
            @Override
            public void returnResult(String Name) {

                SurnameToChange.setText(Name);

            }

        });

        setName.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"menadzer","Imie","Login",login);
        setSurname.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"menadzer","Nazwisko","Login",login);

    }

    public void OnAddUser(View view){
        startActivity(new Intent(ManagerActivity.this,AddUser.class));
    }

    public void OnRemoveUser(View view){
        startActivity(new Intent(ManagerActivity.this,RemoveUser.class));
    }





}
