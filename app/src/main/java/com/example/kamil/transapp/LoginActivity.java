package com.example.kamil.transapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText UsernameET, PasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UsernameET = (EditText) findViewById(R.id.loginText);
        PasswordET = (EditText) findViewById(R.id.passwordText);


    }

    public void OnLogin(View view){

        String username = UsernameET.getText().toString();
        String password = PasswordET.getText().toString();

        String type = "login";

        BackgroundWorker bgWorker = new BackgroundWorker(this, new ResultCheck() {
            @Override
            public void myMethod(String workerType) {
                    switch (workerType) {
                        case "Manager":
                            startActivity(new Intent(LoginActivity.this,ManagerActivity.class));
                            break;
                        case "WarehouseWorker":
                            startActivity(new Intent(LoginActivity.this,WarehouseworkerActivity.class));
                            break;
                        case "Driver":
                            startActivity(new Intent(LoginActivity.this,DriverActivity.class));
                            break;
                    }

            }
        });
        bgWorker.execute(type, username, password);

    }


}
