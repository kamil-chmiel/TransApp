package com.example.kamil.transapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;


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

        BackgroundWorker bgWorker = new BackgroundWorker(this);
        bgWorker.execute(type, username, password);

        //well let's test if this works Kamil
        //blablab



    }





}
