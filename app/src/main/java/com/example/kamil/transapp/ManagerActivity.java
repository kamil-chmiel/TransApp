package com.example.kamil.transapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;


/**
 * Created by Kamil on 26.11.2017.
 */

public class ManagerActivity extends LoginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);


    }

    public void OnAddUser(View view){
            startActivity(new Intent(ManagerActivity.this,AddUser.class));
    }


}
