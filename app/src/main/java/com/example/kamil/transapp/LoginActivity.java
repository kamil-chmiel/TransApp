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


        String temp = UsernameET.getText().toString();
        temp = temp.replaceAll("\\s", "");
        final String username = temp;
        String password = PasswordET.getText().toString();
        DatabaseHandler DBC = new DatabaseHandler();

        String workerType = DatabaseHandler.getInstance().checkLogin(username,password);

                switch (workerType) {
                    case "Manager":
                        setSessionInfo(username, workerType);
                        Intent managerIntent = new Intent(LoginActivity.this, ManagerMenu.class);
                        managerIntent.putExtra("login", username); //passing login to ManagerActivity
                        startActivity(managerIntent);

                        break;

                    case "WarehouseWorker":
                        setSessionInfo(username, workerType);
                        Intent warehouseWorkerIntent = new Intent(LoginActivity.this, WarehouseMenu.class);
                        warehouseWorkerIntent.putExtra("login", username); //passing login to WarehouseworkerActivity
                        startActivity(warehouseWorkerIntent);

                        break;

                    case "Driver":
                        setSessionInfo(username, workerType);
                        Intent driverIntent = new Intent(LoginActivity.this, DriverMenu.class);
                        driverIntent.putExtra("login", username); //passing login to DriverActivity
                        startActivity(driverIntent);

                        break;
                }

    }

    public void setSessionInfo(String login, String type){

        String info[] = DatabaseHandler.getWorkerInfo(login, type);
        if(info.length>0)
        {
            SessionController.setPeselNumber(info[0]);
            SessionController.setAccountType(type);
        }

    }
    /*public void OnLogin(View view){


        String temp = UsernameET.getText().toString();
        temp = temp.replaceAll("\\s", "");
        final String username = temp;
        String password = PasswordET.getText().toString();

        String type = "login";

        UserLogin userLogin = new UserLogin(this, new AsyncResponse() {
            @Override
            public void returnResult(String workerType) {
                    switch (workerType) {
                        case "Manager":

                            Intent managerIntent = new Intent(LoginActivity.this, ManagerMenu.class);
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
        userLogin.execute(type, username, password);

    }*/


}
