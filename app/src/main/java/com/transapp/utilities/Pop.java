package com.transapp.utilities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.databaseHandler.DatabaseHandler;
import com.transapp.R;

/**
 * Created by Kamil on 14.01.2018.
 */

public class Pop extends Activity{


    private static String taskDetails="", type;
    private static TextView descTV;
    private static Button setButton;
    private static String state;

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
        type = bundle.getString("Type");
        state = bundle.getString("State");
        setButton = findViewById(R.id.done_button);

        switch(type) {
            case "Driver":
                if(state.equals("Prepared"))
                {
                    setButton.setText("Set as 'Under way'");
                    setButton.setOnClickListener((View v) -> {
                        try {
                            String[] detailsParts = taskDetails.split(" ");
                            DatabaseHandler.changeTaskState(detailsParts[1], "Under way");
                        } catch (Exception ex) {
                            System.out.println("Bląd podczas wysylania usterki do bazy " + ex.getMessage());
                        } finally {
                            Pop.super.onBackPressed();
                        }
                    });
                }
                if(state.equals("Under way"))
                {
                    setButton.setText("Set as 'Done'");
                    setButton.setOnClickListener((View v) -> {
                        try {
                            String[] detailsParts = taskDetails.split(" ");
                            DatabaseHandler.changeTaskState(detailsParts[1], "Done");
                            DatabaseHandler.changeDriverAvability(SessionController.getAccountType(),SessionController.getPeselNumber(),1);
                        } catch (Exception ex) {
                            System.out.println("Bląd podczas wysylania usterki do bazy " + ex.getMessage());
                        } finally {
                            Pop.super.onBackPressed();
                        }
                    });
                }


                break;
            case "Warehouse":
                setButton.setText("Set as 'Prepared'");
                setButton.setOnClickListener((View v) -> {
                    try {
                        String[] detailsParts = taskDetails.split(" ");
                        DatabaseHandler.changeTaskState(detailsParts[1], "Prepared");
                        DatabaseHandler.changeDriverAvability(SessionController.getAccountType(),SessionController.getPeselNumber(),1);
                    } catch (Exception ex) {
                        System.out.println("Bląd podczas wysylania usterki do bazy " + ex.getMessage());
                    } finally {
                        Pop.super.onBackPressed();
                    }
                });
                break;
        }
        descTV = findViewById(R.id.description_text);
        descTV.setText(taskDetails);
    }

}
