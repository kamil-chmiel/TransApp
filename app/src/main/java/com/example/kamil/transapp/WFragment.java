package com.example.kamil.transapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class WFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private OnFragmentInteractionListener mListener;
    static String login;
    private FloatingActionButton addUserButton;
    private FloatingActionButton removeUserButton;
    private ListView listView;
    private TextView nameToChange, surnameToChange;
    private ArrayList<String> orders;
    private static boolean refreshing = true;

    final Handler wFragmentHandler = new Handler();

    public WFragment() {
        // Required empty public constructor
    }

    public static WFragment newInstance(String log) {
        WFragment fragment = new WFragment();
        Bundle args = new Bundle();
        login = log;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

    }

    /*public void OnResume(){
        listView = (ListView) view.findViewById(R.id.tasks);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,orders);
        listView.setAdapter(adapter);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.w_fragment, container, false);
        refreshData();

        listView = (ListView) view.findViewById(R.id.tasks);

        //UZUPELNIENIE DANYCH USERA
        nameToChange = (TextView) view.findViewById(R.id.show_warehouse_name);
        surnameToChange = (TextView) view.findViewById(R.id.show_warehouse_surname);

        setWarehouseInfo(login);

        // UZUPELNIENIE LISTY TASKOW
        fillActiveTasks();


        /*
        // PODPIECIE BUTTONOW
        addUserButton = view.findViewById(R.id.addUserButton);
        addUserButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent myIntent = new Intent(getContext(), AddUser.class);
                startActivity(myIntent);
            }
        });
        removeUserButton = view.findViewById(R.id.removeUserButton);
        removeUserButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent myIntent = new Intent(getContext(), RemoveUser.class);
                startActivity(myIntent);
            }
        });*/


        return view;
    }

    public void fillActiveTasks() {
        orders = DatabaseHandler.getActiveTasks(SessionController.getAccountType(), SessionController.getPeselNumber());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,orders);
        listView.setAdapter(adapter);
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

    public void refreshData()
    {
        new Thread(new Runnable() {
            public void run() {

                while(refreshing)
                {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    wFragmentHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            fillActiveTasks();
                        }
                    });
                }

            }
        }).start();
    }


    public void setWarehouseInfo(String login){

        String info[] = DatabaseHandler.getWorkerInfo(login,"WarehouseWorker");
        if(info.length>0)
        {
            //SessionController.setPeselNumber(info[0]);
            nameToChange.setText(info[1]);
            surnameToChange.setText(info[2]);
            //sessSessionController.setAccountType("Manager");
        }
    }

}