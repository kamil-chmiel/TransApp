package com.example.kamil.transapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.transapp.broadcastreceiver.*;

import java.util.ArrayList;


public class MFragment extends Fragment implements View.OnClickListener , NetworkConnectionReceiver.NetworkListener {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private OnFragmentInteractionListener mListener;
    static String login;
    private Button settingsButton;
    private static PopupMenu settingsMenu;
    private static ConstraintLayout popUpLayout;
    ListView listView;
    ArrayAdapter<String> adapter;
    TextView nameToChange, surnameToChange;
    ArrayList<String> orders;
    private boolean isRestartRequired = false;
    AlertDialog message;
    Handler handler = new Handler();
    NetworkConnectionReceiver ncr;

    final Handler wFragmentHandler = new Handler();


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
        ncr = new NetworkConnectionReceiver();
        listView = (ListView) view.findViewById(R.id.tasks);

        //UZUPELNIENIE DANYCH USERA
        nameToChange = (TextView) view.findViewById(R.id.manager_name);
        surnameToChange = (TextView) view.findViewById(R.id.manager_surname);
        setManagerInfo(login);

        settingsButton = (Button) view.findViewById(R.id.settings_button_manager_main);
        settingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                showSettingsMenu(v);
            }
        });

        // UZUPELNIENIE LISTY TASKOW
        fillActiveTasks();
        //refreshData();


        return view;
    }

    private void fillActiveTasks() {
        orders = DatabaseHandler.getActiveTasks(SessionController.getAccountType(), SessionController.getPeselNumber());
        if(orders!=null) {
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, orders);
            listView.setAdapter(adapter);
        }
    }

  /*  private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*/



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


    public void setManagerInfo(String login){

        String info[] = DatabaseHandler.getWorkerInfo(login, "Manager");
        if(info.length>0)
        {
            nameToChange.setText(info[1]);
            surnameToChange.setText(info[2]);
        }
    }

    private void showSettingsMenu(View v){

        settingsMenu = new PopupMenu(this.getContext(), v);
        settingsMenu.getMenuInflater().inflate(R.menu.settings_menu_manager, settingsMenu.getMenu());

        settingsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.menu1:
                        Intent addUserIntent = new Intent(getContext(), AddUser.class);
                        startActivity(addUserIntent);
                        break;
                    case R.id.menu2:
                        Intent deleteUserIntent = new Intent(getContext(), RemoveUser.class);
                        startActivity(deleteUserIntent);
                        break;
                    case R.id.menu3:
                        Intent orderHistory = new Intent(getContext(), OrderHistory.class);
                        startActivity(orderHistory);
                        break;
                    case R.id.menu4:
                        handler.removeCallbacks(refreshData);
                        getContext().unregisterReceiver(ncr);
                        Intent logOutIntent = new Intent(getContext(), LoginActivity.class);
                        logOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        logOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        logOutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logOutIntent);
                        break;
                    default:
                        break;

                }
                return true;
            }
        });

        settingsMenu.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
        getContext().registerReceiver(ncr, new android.content.IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister connection status listener
        MyApplication.getInstance().setConnectivityListener(null);
        getContext().unregisterReceiver(ncr);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        getContext().unregisterReceiver(ncr);
        handler.removeCallbacks(refreshData);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
      //  showSnack(isConnected);
        Log.d("onC","iam here!");
        showPopNoInternet(isConnected);
        handleDataRefreshing(isConnected);

    }

    private void showPopNoInternet(boolean isConnected){

         if(isConnected){

        }
        else{
             Intent noNetworkPop = new Intent(getActivity(), NoNetworkPop.class);
             startActivity(noNetworkPop);
        }

    }

    private void handleDataRefreshing(boolean isConnected){

        if(isConnected){
            handler.post(refreshData);
        }

        else{
            handler.removeCallbacks(refreshData);
            isRestartRequired = true;
        }

    }


    private Runnable refreshData = new Runnable() {
        @Override
        public void run() {
            if(isRestartRequired) {
                DatabaseHandler DB = new DatabaseHandler();
                isRestartRequired = false;
            }
            fillActiveTasks();

            handler.postDelayed(refreshData, 5000);
        }
    };

}
