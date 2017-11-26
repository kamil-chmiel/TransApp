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

        String username = UsernameET.getText().toString();
        String password = PasswordET.getText().toString();

        String type = "login";

        BackgroundWorker bgWorker = new BackgroundWorker(this, new ResultCheck() {
            @Override
            public void myMethod(String workerType) {
                    switch (workerType) {
                        case "Menago":
                            startActivity(new Intent(LoginActivity.this,ManagerActivity.class));
                            break;
                        case "Mag":
                            setContentView(R.layout.activity_warehouse_worker);
                            break;
                        case "Kiero":
                            setContentView(R.layout.activity_manager);
                            break;
                    }

            }
        });
        bgWorker.execute(type, username, password);

    }


}
