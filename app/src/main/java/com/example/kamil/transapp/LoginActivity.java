package com.example.kamil.transapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

interface ConnectionChecker {
    void myMethod(boolean result, String workerType);
}

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

        BackgroundWorker bgWorker = new BackgroundWorker(this, new ConnectionChecker() {
            @Override
            public void myMethod(boolean result, String workerType) {
                if(result)
                {

                    switch (workerType) {
                        case "Menago":
                            setContentView(R.layout.activity_manager);
                            break;
                        case "Mag":
                            setContentView(R.layout.activity_manager);
                            break;
                        case "Kiero":
                            setContentView(R.layout.activity_driver);
                            break;
                    }
                }

            }
        });
        bgWorker.execute(type, username, password);

    }


}
