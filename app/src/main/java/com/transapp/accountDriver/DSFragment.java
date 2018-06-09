package com.transapp.accountDriver;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.databaseHandler.DatabaseHandler;
import com.transapp.accountManager.MSFragment;
import com.transapp.R;
import com.transapp.utilities.SessionController;

import java.text.SimpleDateFormat;
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
public class DSFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";
    // TODO: Rename and change types of parameters
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private Button mondayUnavailable, tuesdayUnavailable, wednesdayUnavailable, thursdayUnavailable, fridayUnavailable;
    private TextView Monday,Tuesday,Wednesday,Thursday,Friday, dateTV;

    public DSFragment() {
        // Required empty public constructor
    }


    public static DSFragment newInstance(int nr) {
        DSFragment fragment = new DSFragment();
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

        View view = inflater.inflate(R.layout.d_s_fragment, container, false);

        dateTV = view.findViewById(R.id.dateText);

        Monday = view.findViewById(R.id.monday_schedule);
        Tuesday = view.findViewById(R.id.tuesday_schedule);
        Wednesday = view.findViewById(R.id.wednesday_schedule);
        Thursday = view.findViewById(R.id.thursday_schedule);
        Friday = view.findViewById(R.id.friday_schedule);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("EEEE dd-MMM-yyyy", Locale.ENGLISH);
        String formattedDate = df.format(c.getTime());
        dateTV.setText(formattedDate);
        fillSchedule();

        mondayUnavailable = view.findViewById(R.id.monday_unavailable);
        if(mondayUnavailable.getText().equals("-"))
            mondayUnavailable.setVisibility(View.INVISIBLE);
        mondayUnavailable.setOnClickListener((View v) -> {
            try {
                DatabaseHandler.updateSchedule(SessionController.getPeselNumber(),"Poniedzialek","-");
                Monday.setText("-");
                mondayUnavailable.setVisibility(View.INVISIBLE);
            }
            catch (Exception ex){
                System.out.println("Bląd podczas wysylania usterki do bazy " + ex.getMessage());
            }
            finally {
                AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                Message.setTitle("Message sent!");
                Message.setMessage("Absence has been reported.");
                Message.show();
            }

        });

        tuesdayUnavailable =  view.findViewById(R.id.tuesday_unavailable);
        if(tuesdayUnavailable.getText().equals("-"))
            tuesdayUnavailable.setVisibility(View.INVISIBLE);
        tuesdayUnavailable.setOnClickListener((View v) -> {
            try {
                DatabaseHandler.updateSchedule(SessionController.getPeselNumber(),"Wtorek","-");
                Tuesday.setText("-");
                tuesdayUnavailable.setVisibility(View.INVISIBLE);
            }
            catch (Exception ex){
                System.out.println("Bląd podczas wysylania usterki do bazy " + ex.getMessage());
            }
            finally {
                AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                Message.setTitle("Message sent!");
                Message.setMessage("Absence has been reported.");
                Message.show();
            }

        });
        wednesdayUnavailable =  view.findViewById(R.id.wednesday_unavailable);
        if(wednesdayUnavailable.getText().equals("-"))
            wednesdayUnavailable.setVisibility(View.INVISIBLE);
        wednesdayUnavailable.setOnClickListener((View v) -> {
            try {
                DatabaseHandler.updateSchedule(SessionController.getPeselNumber(),"Sroda","-");
                Wednesday.setText("-");
                wednesdayUnavailable.setVisibility(View.INVISIBLE);
            }
            catch (Exception ex){
                System.out.println("Bląd podczas wysylania usterki do bazy " + ex.getMessage());
            }
            finally {
                AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                Message.setTitle("Message sent!");
                Message.setMessage("Absence has been reported.");
                Message.show();
            }

        });
        thursdayUnavailable = view.findViewById(R.id.thursday_unavailable);
        if(thursdayUnavailable.getText().equals("-"))
            thursdayUnavailable.setVisibility(View.INVISIBLE);
        thursdayUnavailable.setOnClickListener((View v) -> {
            try {
                DatabaseHandler.updateSchedule(SessionController.getPeselNumber(),"Czwartek","-");
                Thursday.setText("-");
                thursdayUnavailable.setVisibility(View.INVISIBLE);
            }
            catch (Exception ex){
                System.out.println("Bląd podczas wysylania usterki do bazy " + ex.getMessage());
            }
            finally {
                AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                Message.setTitle("Message sent!");
                Message.setMessage("Absence has been reported.");
                Message.show();
            }

        });
        fridayUnavailable =  view.findViewById(R.id.friday_unavailable);
        if(fridayUnavailable.getText().equals("-"))
            fridayUnavailable.setVisibility(View.INVISIBLE);
        fridayUnavailable.setOnClickListener((View v) -> {
            try {
                DatabaseHandler.updateSchedule(SessionController.getPeselNumber(),"Piatek","-");
                Friday.setText("-");
                fridayUnavailable.setVisibility(View.INVISIBLE);
            }
            catch (Exception ex){
                System.out.println("Bląd podczas wysylania usterki do bazy " + ex.getMessage());
            }
            finally {
                AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                Message.setTitle("Message sent!");
                Message.setMessage("Absence has been reported.");
                Message.show();
            }

        });

        return view;
    }

    private void fillSchedule() {
        String[] data = DatabaseHandler.getSchedule(SessionController.getPeselNumber());
        Monday.setText(data[0]);
        Tuesday.setText(data[1]);
        Wednesday.setText(data[2]);
        Thursday.setText(data[3]);
        Friday.setText(data[4]);
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
}
