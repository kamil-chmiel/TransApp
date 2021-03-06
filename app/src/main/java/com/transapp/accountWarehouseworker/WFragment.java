package com.transapp.accountWarehouseworker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.databaseHandler.DatabaseHandler;
import com.transapp.loginHandler.LoginActivity;
import com.networkConnectivityHandler.NoNetworkPop;
import com.transapp.utilities.Pop;
import com.transapp.R;
import com.transapp.utilities.SessionController;
import com.networkConnectivityHandler.MyApplication;
import com.networkConnectivityHandler.NetworkConnectionReceiver;

import java.util.ArrayList;

public class WFragment extends Fragment implements View.OnClickListener , NetworkConnectionReceiver.NetworkListener {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private OnFragmentInteractionListener mListener;
    static String login;
    private ListView listView;
    private TextView nameToChange, surnameToChange;
    private ArrayList<String> orders;
    private Button settingsButton;
    private PopupMenu settingsMenu;
    AlertDialog message;
    Handler handler = new Handler();
    NetworkConnectionReceiver ncr;
    private boolean isRestartRequired = false;


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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.w_fragment, container, false);
        ncr = new NetworkConnectionReceiver();

        listView =  view.findViewById(R.id.tasks);

        //UZUPELNIENIE DANYCH USERA
        nameToChange =  view.findViewById(R.id.warehouse_name);
        surnameToChange =  view.findViewById(R.id.warehouse_surname);

        setWarehouseInfo(login);

        settingsButton = view.findViewById(R.id.settings_button_warehouse_main);
        settingsButton.setOnClickListener(this::showSettingsMenu);


        // UZUPELNIENIE LISTY TASKOW
        fillActiveTasks();

        // KLIKANIE TASKU

        listView.setClickable(true);
        listView.setOnItemClickListener((AdapterView<?> arg0, View arg1, int position, long arg3) -> {

            Object o = listView.getItemAtPosition(position);

            Intent popUpIntent = new Intent(getActivity(), Pop.class);
            popUpIntent.putExtra("Details", o.toString());
            popUpIntent.putExtra("Type", "Warehouse");
            startActivity(popUpIntent);
        });

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


    public void setWarehouseInfo(String login){

        String info[] = DatabaseHandler.getWorkerInfo(login,"WarehouseWorker");
        if(info.length>0)
        {
            nameToChange.setText(info[1]);
            surnameToChange.setText(info[2]);

        }
    }

    private void showSettingsMenu(View v){

        settingsMenu = new PopupMenu(this.getContext(), v);
        settingsMenu.getMenuInflater().inflate(R.menu.settings_menu_warehouse, settingsMenu.getMenu());

        settingsMenu.setOnMenuItemClickListener((MenuItem menuItem) -> {

                switch (menuItem.getItemId()) {

                    case R.id.menu1:
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

        if(!isConnected){
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
