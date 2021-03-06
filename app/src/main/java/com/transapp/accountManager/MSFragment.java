package com.transapp.accountManager;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.databaseHandler.DatabaseHandler;
import com.transapp.R;
import com.transapp.utilities.SessionController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MSFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MSFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String[] schedule = new String[5];
    private ArrayList<String> users = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private TextView Monday,Tuesday,Wednesday,Thursday,Friday, dateTV;
    private EditText MondayEdit, TuesdayEdit ,WednesdayEdit,ThursdayEdit,FridayEdit;
    private ViewSwitcher mondaySwitcher, tuesdaySwitcher, wednesdaySwitcher, thursdaySwitcher, fridaySwitcher;
    private Spinner userSpinner;

    public MSFragment() {
        // Required empty public constructor
    }


    public static MSFragment newInstance(int nr) {
        MSFragment fragment = new MSFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, nr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.m_s_fragment, container, false);

        dateTV = view.findViewById(R.id.dateText);

        userSpinner = view.findViewById(R.id.user_spinner);
        //uzueplenienie spinnera uzytkownikami
        ArrayList<String> newUsers = DatabaseHandler.getInstance().getAllWorkers();
        users.add(SessionController.getPeselNumber() + " My Schedule");
        for(int i=0; i<newUsers.size(); i++)
            users.add(newUsers.get(i));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String[] parts = userSpinner.getSelectedItem().toString().split(" ");
                String[] days = DatabaseHandler.getSchedule(parts[0]);

                Monday.setText(days[0]);
                Tuesday.setText(days[1]);
                Wednesday.setText(days[2]);
                Thursday.setText(days[3]);
                Friday.setText(days[4]);

                schedule=days;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        // ZMIANA HARMONOGRAMU

        mondaySwitcher =  view.findViewById(R.id.monday_switcher);
        tuesdaySwitcher = view.findViewById(R.id.tuesday_switcher);
        wednesdaySwitcher =  view.findViewById(R.id.wednesday_switcher);
        thursdaySwitcher =  view.findViewById(R.id.thursday_switcher);
        fridaySwitcher =  view.findViewById(R.id.friday_switcher);

        Monday = view.findViewById(R.id.monday_schedule);
        MondayEdit = view.findViewById(R.id.hidden_monday_edit_text);
        Monday.setOnClickListener((View v) -> {
            mondaySwitcher.showNext();
            MondayEdit.setText(schedule[0]);

            MondayEdit.setOnKeyListener((View v1, int keyCode, KeyEvent event) -> {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    if(checkFormat(MondayEdit.getText().toString().trim())){
                    Monday.setText(MondayEdit.getText());
                    schedule[0] = MondayEdit.getText().toString();
                    mondaySwitcher.showPrevious();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    Log.d("sch",schedule[0]);


                        String[] parts = userSpinner.getSelectedItem().toString().split(" ");
                        DatabaseHandler.updateSchedule(parts[0],"Poniedzialek", MondayEdit.getText().toString());
                        Toast.makeText(getContext(),"Schedule saved!", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    else{
                        Toast.makeText(getContext(),"Wrong format!",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                return false;
            });
        });

        Tuesday = view.findViewById(R.id.tuesday_schedule);
        TuesdayEdit = view.findViewById(R.id.hidden_tuesday_edit_text);
        Tuesday.setOnClickListener((View v) -> {
            tuesdaySwitcher.showNext();
            TuesdayEdit.setText(schedule[1]);

            TuesdayEdit.setOnKeyListener((View v12, int keyCode, KeyEvent event) -> {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Tuesday.setText(TuesdayEdit.getText());
                    schedule[1] = TuesdayEdit.getText().toString();
                    tuesdaySwitcher.showPrevious();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    if(checkFormat(TuesdayEdit.getText().toString().trim())){

                        String[] parts = userSpinner.getSelectedItem().toString().split(" ");
                        DatabaseHandler.updateSchedule(parts[0],"Wtorek", TuesdayEdit.getText().toString());
                        Toast.makeText(getContext(),"Schedule saved!", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    else{
                        Toast.makeText(getContext(),"Wrong format!",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                return false;
            });
        });
        Wednesday = view.findViewById(R.id.wednesday_schedule);
        WednesdayEdit = view.findViewById(R.id.hidden_wednesday_edit_text);
        Wednesday.setOnClickListener((View v) -> {
            wednesdaySwitcher.showNext();
            WednesdayEdit.setText(schedule[2]);

            WednesdayEdit.setOnKeyListener((View v13, int keyCode, KeyEvent event) -> {
                // If the event is a key-down event on the "enter" button

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Wednesday.setText(WednesdayEdit.getText());
                    schedule[2] = WednesdayEdit.getText().toString();
                    wednesdaySwitcher.showPrevious();

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    if(checkFormat(WednesdayEdit.getText().toString().trim())){

                        String[] parts = userSpinner.getSelectedItem().toString().split(" ");
                        DatabaseHandler.updateSchedule(parts[0],"Sroda", WednesdayEdit.getText().toString());
                        Toast.makeText(getContext(),"Schedule saved!", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    else{
                        Toast.makeText(getContext(),"Wrong format!",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                return false;
            });
        });
        Thursday = view.findViewById(R.id.thursday_schedule);
        ThursdayEdit = view.findViewById(R.id.hidden_thursday_edit_text);
        Thursday.setOnClickListener((View v) -> {

            thursdaySwitcher.showNext();
            ThursdayEdit.setText(schedule[3]);

            ThursdayEdit.setOnKeyListener((View v14, int keyCode, KeyEvent event) -> {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Thursday.setText(ThursdayEdit.getText());
                    schedule[3] = ThursdayEdit.getText().toString();
                    thursdaySwitcher.showPrevious();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    if(checkFormat(ThursdayEdit.getText().toString().trim())){

                            String[] parts = userSpinner.getSelectedItem().toString().split(" ");
                            DatabaseHandler.updateSchedule(parts[0],"Czwartek", ThursdayEdit.getText().toString());
                            Toast.makeText(getContext(),"Schedule saved!", Toast.LENGTH_LONG).show();
                            return true;
                    }
                    else{
                        Toast.makeText(getContext(),"Wrong format!",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                return false;
            });
        });
        Friday = view.findViewById(R.id.friday_schedule);
        FridayEdit = view.findViewById(R.id.hidden_friday_edit_text);
        Friday.setOnClickListener((View v) -> {
            fridaySwitcher.showNext();
            FridayEdit.setText(schedule[4]);

            FridayEdit.setOnKeyListener((View v15, int keyCode, KeyEvent event) -> {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Friday.setText(FridayEdit.getText());
                    schedule[4] = FridayEdit.getText().toString();
                    fridaySwitcher.showPrevious();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    if(checkFormat(FridayEdit.getText().toString().trim())){

                        String[] parts = userSpinner.getSelectedItem().toString().split(" ");
                        DatabaseHandler.updateSchedule(parts[0],"Piatek", FridayEdit.getText().toString());
                        Toast.makeText(getContext(),"Schedule saved!", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    else{
                        Toast.makeText(getContext(),"Wrong format!",Toast.LENGTH_LONG).show();
                        return false;
                    }

                }
                return false;
            });
        });

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("EEEE dd-MMM-yyyy", Locale.ENGLISH);
        String formattedDate = df.format(c.getTime());
        dateTV.setText(formattedDate);
        fillSchedule();


        return view;
    }
    


    private void fillSchedule() {

        String[] data = DatabaseHandler.getSchedule(SessionController.getPeselNumber());
        Monday.setText(data[0]);
        Tuesday.setText(data[1]);
        Wednesday.setText(data[2]);
        Thursday.setText(data[3]);
        Friday.setText(data[4]);

        schedule=data;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private boolean checkFormat(String field){

        Log.d("date",field);
        return field.matches("^([0-1][0-9]|[2][0-3]):([0-5][0-9])-([0-1][0-9]|[2][0-3]):([0-5][0-9])");

    }
}
