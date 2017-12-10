package com.example.kamil.transapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Adams on 2017-11-27.
 * Removes user based on login
 */

public class RemoveUser extends ManagerActivity {

    EditText usernameToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);
        usernameToDelete = (EditText) findViewById(R.id.usernameToDeleteText);

    }

    public void OnRemove(View view)
    {
        String user_name = usernameToDelete.getText().toString();

        String type = "unregister";

        UserLogin userLogin = new UserLogin(this, new AsyncResponse() {
            @Override
            public void returnResult(String result) {
                usernameToDelete.setText("");
            }
        });

        userLogin.execute(type, user_name);
    }

    @Override
    public void getUserInfo() {
        // overriding so RemoveUser wont try to set user name and surname
    }
}

