package com.example.kamil.transapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


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
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private TextView scheduleTV, dateTV;

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
        scheduleTV = (TextView)view.findViewById(R.id.scheduleText);
        dateTV = (TextView)view.findViewById(R.id.dateText);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("EEEE dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        dateTV.setText(formattedDate);
        fillSchedule(scheduleTV);


        return view;
    }

    private void fillSchedule(TextView fieldToFill) {
        String[] data = DatabaseHandler.getSchedule(SessionController.getPeselNumber());
        fieldToFill.setText("Monday: "+data[0]+"\nTuesday: "+data[1]+"\nWednesday: "+data[2]+"\nThursday: "+data[3]+"\nFriday: "+data[4]);
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
