package com.example.kamil.transapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText UsernameET, PasswordET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UsernameET = findViewById(R.id.loginText);
        PasswordET = findViewById(R.id.passwordText);


    }
    public void OnLogin(View view){


        String temp = UsernameET.getText().toString();
        temp = temp.replaceAll("\\s", "");
        final String username = temp;
        String password = PasswordET.getText().toString();
        if(isNetworkAvailable()) {

            DatabaseHandler DBC = new DatabaseHandler();
            String workerType = DatabaseHandler.getInstance().checkLogin(username, password);

                switch (workerType) {
                    case "Manager":
                        setSessionInfo(username, workerType);
                        Intent managerIntent = new Intent(LoginActivity.this, ManagerMenu.class);
                        managerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        managerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        managerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        managerIntent.putExtra("login", username); //passing login to ManagerActivity
                        startActivity(managerIntent);

                        break;

                    case "WarehouseWorker":
                        setSessionInfo(username, workerType);
                        Intent warehouseWorkerIntent = new Intent(LoginActivity.this, WarehouseMenu.class);
                        warehouseWorkerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        warehouseWorkerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        warehouseWorkerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        warehouseWorkerIntent.putExtra("login", username); //passing login to WarehouseworkerActivity
                        startActivity(warehouseWorkerIntent);

                        break;

                    case "Driver":
                        setSessionInfo(username, workerType);
                        Intent driverIntent = new Intent(LoginActivity.this, DriverMenu.class);
                        driverIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        driverIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        driverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        driverIntent.putExtra("login", username); //passing login to DriverActivity
                        startActivity(driverIntent);

                        break;
                    default:
                        Toast.makeText(this, "Couldn't Log In! Check your credentials!", Toast.LENGTH_LONG).show();
                        break;
                }

        }
        else
            Toast.makeText(this, "Couldn't Connect to database!\nCheck your network connection!", Toast.LENGTH_LONG).show();
    }

    public void setSessionInfo(String login, String type){

        String info[] = DatabaseHandler.getWorkerInfo(login, type);
        if(info.length>0)
        {
            SessionController.setPeselNumber(info[0]);
            SessionController.setAccountType(type);
        }

    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
