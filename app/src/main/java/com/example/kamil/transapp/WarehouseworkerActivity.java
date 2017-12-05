package com.example.kamil.transapp;


import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Created by Kamil on 26.11.2017.
 * WarehouseWorker Activity class
 */

public class WarehouseworkerActivity extends LoginActivity {

    TextView NameToChange, SurnameToChange;

    String login;

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


        NameToChange = (TextView) findViewById(R.id.show_warehouse_worker_name);
        SurnameToChange = (TextView) findViewById(R.id.show_warehouse_worker_surname);

        Bundle b = getIntent().getExtras();
        if(b != null)
            login = b.getString("login");


        listView = (ListView) findViewById(R.id.tasks);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,orders);
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

        setName.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"pracownik_magazynu","Imie","Login",login);
        setSurname.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"pracownik_magazynu","Nazwisko","Login",login);


    }



}
