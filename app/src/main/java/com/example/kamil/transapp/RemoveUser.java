package com.example.kamil.transapp;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Adams on 2017-11-27.
 * Removes user based on login
 */

public class RemoveUser extends ManagerMenu  implements View.OnClickListener {

    EditText usernameToDelete;
    private Button removeUserButton;

    private enum States{
        NOT_EXISTS,USERNAME, OK
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);
        usernameToDelete = findViewById(R.id.usernameToDeleteText);
        removeUserButton = findViewById(R.id.removeUserFromBase);

        removeUserButton.setOnClickListener((View v)->
        {
            String user_name = usernameToDelete.getText().toString();

            States state = checkFields();

            switch (state){


                case USERNAME:
                    Toast.makeText(this, "Please state username!", Toast.LENGTH_LONG).show();
                    break;

                case NOT_EXISTS:
                    Toast.makeText(this, "User does not exists in database!", Toast.LENGTH_LONG).show();
                    break;

                case OK:

                    DatabaseHandler.deleteUser(user_name);
                    Snackbar.make(v,"User has been deleted from database !", Snackbar.LENGTH_LONG).show();
                    usernameToDelete.setText("");

                    break;
            }



        });

    }



    private States checkFields(){

        States state = States.OK;

        if(usernameToDelete.getText().toString().matches(""))
            state = States.USERNAME;

        if(!DatabaseHandler.isExisitingUser(usernameToDelete.getText().toString().trim()))
            state = States.NOT_EXISTS;

        return state;
    }


    @Override
    public void onClick(View v) {

    }

}

