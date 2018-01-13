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
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class WTFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Button addTaskButton;
    private ArrayList<String> workers = new ArrayList<String>();
    private boolean readData = true;
    private String orderNum;
    private ArrayList<Task> tasks;
    private Spinner workersSpinner;
    private Spinner customerSpinner;
    private Spinner driverSpinner;
    private TextView orderTV;
    private TextView itemsTV;
    private TextView describtion;
    private TextView deadline;

    public WTFragment() {
        // Required empty public constructor
    }

    public static WTFragment newInstance(int nr) {
        WTFragment fragment = new WTFragment();
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
        View view = inflater.inflate(R.layout.w_t_fragment, container, false);

        TabHost tab = (TabHost) view.findViewById(R.id.tabHost);
        tab.setup();

        TabHost.TabSpec spec1 = tab.newTabSpec("Tab 1");
        spec1.setIndicator("Add amount");
        spec1.setContent(R.id.Add_amount);
        tab.addTab(spec1);

        TabHost.TabSpec spec2 = tab.newTabSpec("Tab 2");
        spec2.setIndicator("Add new item");
        spec2.setContent(R.id.Add_new_item);
        tab.addTab(spec2);

        TabHost.TabSpec spec3 = tab.newTabSpec("Tab 3");
        spec3.setIndicator("Delete item");
        spec3.setContent(R.id.Delete_item);
        tab.addTab(spec3);


        /*workersSpinner = (Spinner) view.findViewById(R.id.workersSpinner);
        customerSpinner = (Spinner) view.findViewById(R.id.customerSpinner);
        driverSpinner = (Spinner) view.findViewById(R.id.driverSpinner);
        orderTV = (TextView) view.findViewById(R.id.orderNum);
        itemsTV = (TextView) view.findViewById(R.id.itemsText);
        describtion = (TextView) view.findViewById(R.id.describtionText);
        deadline = (TextView) view.findViewById(R.id.deadlineText);
        orderNum = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        orderTV.setText("Order no. #"+ orderNum);

        fillAvailableWorkers(workersSpinner, customerSpinner, driverSpinner);


        addTaskButton = (Button) view.findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String pesel = customerSpinner.getSelectedItem().toString();
                pesel = pesel.substring(0, pesel.indexOf(" "));
                String[] customerInfo = DatabaseHandler.getWorkerDetails("klient", pesel);
                Customer newCustomer = new Customer(customerInfo[0],customerInfo[1],customerInfo[2],customerInfo[3]);

                pesel = workersSpinner.getSelectedItem().toString();
                pesel = pesel.substring(0, pesel.indexOf(" "));
                String[] workerInfo = DatabaseHandler.getWorkerDetails("pracownik_magazynu", pesel);
                WarehouseWorker newWorker = new WarehouseWorker(workerInfo[0],workerInfo[1],workerInfo[2]);

                pesel = driverSpinner.getSelectedItem().toString();
                pesel = pesel.substring(0, pesel.indexOf(" "));
                String[] driverInfo = DatabaseHandler.getWorkerDetails("kierowca", pesel);
                Driver newDriver = new Driver(driverInfo[0],driverInfo[1],driverInfo[2]);

                Task newTask = new Task(orderNum, itemsTV.getText().toString(),
                        describtion.getText().toString(), deadline.getText().toString(), newCustomer, newWorker, newDriver);

                try {
                    SessionController.addTask(newTask);
                }
                catch (Exception ex){
                    System.out.println("Bląd podczas wysylania taska do bazy " + ex.getMessage());
                }
                finally {
                    AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                    Message.setTitle("Task saved!");
                    Message.setMessage("Task #"+orderNum+" has been saved!");
                    Message.show();
                }

                newTask.sendToDataBase();
                clearForm();
            }
        });*/
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void clearForm()
    {
        orderNum = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        orderTV.setText("Order no. #"+ orderNum);
        itemsTV.setText("");
        describtion.setText("");
        deadline.setText("");

        fillAvailableWorkers(workersSpinner, customerSpinner, driverSpinner);
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



    public void fillAvailableWorkers(Spinner workersSpinner, Spinner customerSpinner, Spinner driverSpinner)
    {
        workers = DatabaseHandler.getInstance().getAvailableWorkers();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, workers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workersSpinner.setAdapter(adapter);

        ArrayAdapter<String> customerAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, DatabaseHandler.getInstance().getCustomers());
        customerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customerSpinner.setAdapter(customerAdapter);

        ArrayAdapter<String> driverAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, DatabaseHandler.getInstance().getAvailableDrivers());
        driverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driverSpinner.setAdapter(driverAdapter);
    }

}
