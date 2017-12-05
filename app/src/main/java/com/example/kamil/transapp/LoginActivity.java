package com.example.kamil.transapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        final String username = UsernameET.getText().toString();
        String password = PasswordET.getText().toString();

        String type = "login";

        BackgroundWorker bgWorker = new BackgroundWorker(this, new AsyncResponse() {
            @Override
            public void returnResult(String workerType) {
                    switch (workerType) {
                        case "Manager":

                            Intent managerIntent = new Intent(LoginActivity.this, ManagerActivity.class);
                            managerIntent.putExtra("login", username); //passing login to ManagerActivity
                            startActivity(managerIntent);

                            break;

                        case "WarehouseWorker":

                            Intent warehouseWorkerIntent = new Intent(LoginActivity.this, WarehouseworkerActivity.class);
                            warehouseWorkerIntent.putExtra("login", username); //passing login to WarehouseworkerActivity
                            startActivity(warehouseWorkerIntent);

                            break;

                        case "Driver":

                            Intent driverIntent = new Intent(LoginActivity.this, DriverActivity.class);
                            driverIntent.putExtra("login", username); //passing login to DriverActivity
                            startActivity(driverIntent);

                            break;
                    }

            }
        });
        bgWorker.execute(type, username, password);

    }


}
