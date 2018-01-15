package com.example.kamil.transapp;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class DTFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Button addFaultButton;
    private ArrayList<String> cars = new ArrayList<String>();
    private boolean readData = true;
    private String orderNum;
    private ArrayList<Task> tasks;
    private Spinner carsSpinner;
    private EditText describtion;

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

        ListView carsLV = view.findViewById(R.id.cars_list);

        ArrayList<String> cars = DatabaseHandler.getAvailableCars();
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,cars);
        carsLV.setAdapter(adapter);

        describtion = (EditText) view.findViewById(R.id.description_text);
        carsSpinner = (Spinner) view.findViewById(R.id.cars_spinner);
        fillCars();

        addFaultButton = (Button) view.findViewById(R.id.set_fault_button);
        addFaultButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    String[] parts = carsSpinner.getSelectedItem().toString().split(" ");
                    DatabaseHandler.addCarFault(parts[2]+" "+parts[3], describtion.getText().toString());
                    fillCars();
                }
                catch (Exception ex){
                    System.out.println("Bląd podczas wysylania usterki do bazy " + ex.getMessage());
                }
                finally {
                    AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                    Message.setTitle("Fault sent!");
                    Message.setMessage("Car fault has been sent to the base!");
                    Message.show();
                }

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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, cars);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carsSpinner.setAdapter(adapter);
    }

}
