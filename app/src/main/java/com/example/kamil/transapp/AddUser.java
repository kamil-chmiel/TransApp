package com.example.kamil.transapp;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class AddUser extends ManagerActivity {

    EditText username, password;
    Spinner workerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        username = (EditText) findViewById(R.id.usernameText);
        password = (EditText) findViewById(R.id.passwordText);
        workerType = (Spinner) findViewById(R.id.typeBox);

        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<String>(AddUser.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.types));

        dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workerType.setAdapter(dropDownAdapter);
        Log.d("Mytag", workerType.toString());
    }


    public void OnAdd(View view)
    {
        String user_name = username.getText().toString();
        String _password = password.getText().toString();
        String worker_type = workerType.getSelectedItem().toString();



        String type = "register";
        BackgroundWorker bgWorker = new BackgroundWorker(this, new ResultCheck() {
            @Override
            public void myMethod(String result) {
                username.setText("");
                password.setText("");
            }
        });

        bgWorker.execute(type, user_name, _password, worker_type);
    }
}
