package com.example.kamil.transapp;



import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class DriverActivity extends LoginActivity {

    TextView NameToChange, SurnameToChange;

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
        setContentView(R.layout.activity_driver);

        NameToChange = (TextView) findViewById(R.id.show_driver_name);
        SurnameToChange = (TextView) findViewById(R.id.show_driver_surname);

        Bundle b = getIntent().getExtras();
        String login = b.getString("login");

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

        setName.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"kierowca","Imie","Login",login);
        setSurname.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"kierowca","Nazwisko","Login",login);



    }



}
