package com.example.kamil.transapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Kamil on 14.01.2018.
 */

public class Pop extends Activity{


    private static String taskDetails="";
    private static TextView descTV;
    private static Button setButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_up_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        taskDetails = bundle.getString("Details");

        setButton = findViewById(R.id.done_button);
        setButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    String[] detailsParts = taskDetails.split(" ");
                    DatabaseHandler.changeTaskState(detailsParts[1],"Done");
                }
                catch (Exception ex){
                    System.out.println("Bląd podczas wysylania usterki do bazy " + ex.getMessage());
                }
                finally {
                    Pop.super.onBackPressed();
                }

            }
        });

        descTV = findViewById(R.id.describtion_text);
        descTV.setText(taskDetails);
    }

}
