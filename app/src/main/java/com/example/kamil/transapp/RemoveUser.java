package com.example.kamil.transapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Adams on 2017-11-27.
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

        BackgroundWorker bgWorker = new BackgroundWorker(this, new AsyncResponse() {
            @Override
            public void returnResult(String result) {
                usernameToDelete.setText("");
            }
        });

        bgWorker.execute(type, user_name);
    }
}

