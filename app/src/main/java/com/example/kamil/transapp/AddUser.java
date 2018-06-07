package com.example.kamil.transapp;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class AddUser extends ManagerMenu  implements View.OnClickListener {

    EditText username, password;
    Spinner workerType;
    private Button addUserButton;

    private enum States{
        EXISTS,USERNAME,PASSWORD, OK
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        username = findViewById(R.id.usernameText);
        password =  findViewById(R.id.passwordText);
        workerType =  findViewById(R.id.typeBox);
        addUserButton = findViewById(R.id.addUserToBase);

        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<>(AddUser.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.types));

        dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workerType.setAdapter(dropDownAdapter);

        addUserButton.setOnClickListener((View v) ->
        {
            String user_name = username.getText().toString();
            String _password = password.getText().toString();
            String worker_type = workerType.getSelectedItem().toString();

            States state = checkFields();

            switch (state){


                case USERNAME:
                    Toast.makeText(this, "Please state username!", Toast.LENGTH_LONG).show();
                    break;

                case PASSWORD:
                    Toast.makeText(this, "Please state password!", Toast.LENGTH_LONG).show();
                    break;

                case EXISTS:
                    Toast.makeText(this, "User exists in database!", Toast.LENGTH_LONG).show();
                    break;

                case OK:

                    DatabaseHandler.addNewUser(user_name, _password, worker_type);
                    username.setText("");
                    password.setText("");
                    Snackbar.make(v,"User has been added to database !", Snackbar.LENGTH_LONG).show();

                    break;
            }

        });



    }



    private States checkFields(){

        States state = States.OK;

        if(password.getText().toString().matches(""))
            state = States.PASSWORD;
        if(username.getText().toString().matches(""))
            state = States.USERNAME;
        if(DatabaseHandler.isExisitingUser(username.getText().toString().trim()))
            state = States.EXISTS;

        return state;
    }

    @Override
    public void onClick(View v) {

    }

}



