package com.example.kamil.transapp;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;



public class AddUser extends ManagerActivity {

    EditText username, password, workerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        username = (EditText) findViewById(R.id.usernameToDeleteText);
        password = (EditText) findViewById(R.id.passwordText);
        workerType = (EditText) findViewById(R.id.typeText);
    }

    public void OnAdd(View view)
    {
        String user_name = username.getText().toString();
        String _password = password.getText().toString();
        String worker_type = workerType.getText().toString();

        String type = "register";

        BackgroundWorker bgWorker = new BackgroundWorker(this, new ResultCheck() {
            @Override
            public void myMethod(String result) {
                username.setText("");
                password.setText("");
                workerType.setText("");
            }
        });

        bgWorker.execute(type, user_name, _password, worker_type);
    }
}
