package com.example.kamil.transapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Adams on 2017-11-27.
 * Removes user based on login
 */

public class RemoveUser extends ManagerMenu {

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

        DatabaseHandler.deleteUser(user_name);
        AlertDialog Message = new AlertDialog.Builder(this).create();
        Message.setTitle("User removed");
        Message.setMessage("User has been removed from the base!");
        Message.show();
        usernameToDelete.setText("");
    }

}

