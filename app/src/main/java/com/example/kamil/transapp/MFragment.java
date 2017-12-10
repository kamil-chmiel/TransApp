package com.example.kamil.transapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private OnFragmentInteractionListener mListener;
    static String login;
    private FloatingActionButton addUserButton;
    private FloatingActionButton removeUserButton;
    ListView listView;
    ArrayAdapter<String> adapter;
    TextView NameToChange, SurnameToChange;
    String[] orders = {
            "Order #120001 24-11-2017 11:31:32",
            "Order #120249 24-11-2017 9:12:11",
            "Order #127233 25-11-2017 16:25:42",
            "Order #124641 26-11-2017 10:46:01",
            "Order #127820 27-11-2017 8:54:55"
    };

    public MFragment() {
        // Required empty public constructor
    }

    public static MFragment newInstance(String log) {
        MFragment fragment = new MFragment();
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
        View view = inflater.inflate(R.layout.m_fragment, container, false);



        //UZUPELNIENIE DANYCH USERA
        NameToChange = (TextView) view.findViewById(R.id.show_manager_name);
        SurnameToChange = (TextView) view.findViewById(R.id.show_manager_surname);
        getUserInfo();

        // UZUPELNIENIE LISTY TASKOW
        listView = (ListView) view.findViewById(R.id.tasks);
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,orders);
        listView.setAdapter(adapter);

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


    public void getUserInfo(){

        GetDataFromDatabase setName = new GetDataFromDatabase(getContext(), new AsyncResponse(){
            @Override
            public void returnResult(String Name) {

                NameToChange.setText(Name);

            }

        });

        GetDataFromDatabase setSurname = new GetDataFromDatabase(getContext(), new AsyncResponse(){
            @Override
            public void returnResult(String Name) {

                SurnameToChange.setText(Name);

            }

        });

        setName.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "menadzer", "Imie", "Login", login);
        setSurname.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "menadzer", "Nazwisko", "Login", login);
    }

}
