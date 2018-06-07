package com.example.kamil.transapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;

public class DTFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Button addFaultButton;
    private ArrayList<String> cars = new ArrayList<String>();
    private ArrayList<String> cars2 = new ArrayList<String>();
    private boolean readData = true;
    private String orderNum;
    private ArrayList<Task> tasks;
    private ListView carsLV;
    private Spinner carsSpinner;
    private EditText description;

    public DTFragment() {
        // Required empty public constructor
    }

    public static DTFragment newInstance(int nr) {
        DTFragment fragment = new DTFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, nr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.d_t_fragment, container, false);

        TabHost tab = (TabHost) view.findViewById(R.id.tabHost);
        tab.setup();

        TabHost.TabSpec spec1 = tab.newTabSpec("Tab 1");
        spec1.setIndicator("Set fault");
        spec1.setContent(R.id.Set_fault);
        tab.addTab(spec1);

        TabHost.TabSpec spec2 = tab.newTabSpec("Tab 2");
        spec2.setIndicator("Available cars");
        spec2.setContent(R.id.Show_cars);
        tab.addTab(spec2);

        carsLV = view.findViewById(R.id.cars_list);
        description = view.findViewById(R.id.description_text);
        carsSpinner = view.findViewById(R.id.cars_spinner);

        fillCars();

        addFaultButton = view.findViewById(R.id.set_fault_button);
        addFaultButton.setOnClickListener((View v) ->
        {
            if(description.getText().toString().matches("|0"))
                Toast.makeText(this.getContext(), "Please describe the fault !", Toast.LENGTH_LONG).show();
            else {
                String[] parts = carsSpinner.getSelectedItem().toString().split(" ");
                DatabaseHandler.addCarFault(parts[2] + " " + parts[3], description.getText().toString());

                showSuccesSnackbar("The fault for: " + carsSpinner.getSelectedItem().toString() + " has been added !");

                fillCars();
                fillAvailableCars();
            }


        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    public void fillCars()
    {
        cars = DatabaseHandler.getCars();

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, cars);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carsSpinner.setAdapter(adapterSpinner);

    }

    public void fillAvailableCars()
    {
        cars2 = DatabaseHandler.getAvailableCars();
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,cars2);
        carsLV.setAdapter(adapter);

    }

    private void showSuccesSnackbar(String snackText){

        Snackbar successSnackbar = Snackbar.make(getActivity().findViewById(R.id.d_t_linearlayout),snackText, Snackbar.LENGTH_LONG);


        View viewS = successSnackbar.getView();
        android.widget.TextView tv = viewS.findViewById(android.support.design.R.id.snackbar_text);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        } else {
            tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        }

        successSnackbar.show();

    }

}
