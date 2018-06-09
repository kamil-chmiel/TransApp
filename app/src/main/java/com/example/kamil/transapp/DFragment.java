package com.example.kamil.transapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

public class DFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private OnFragmentInteractionListener mListener;
    static String login;
    private FloatingActionButton addUserButton;
    private FloatingActionButton removeUserButton;
    private String orderNum, customer, address;
    private Button settingsButton;
    private PopupMenu settingsMenu;
    ListView listView;
    ArrayAdapter<String> adapter;
    TextView nameToChange, surnameToChange;
    ArrayList<String> orders;
    AlertDialog message;

    private static boolean refreshing = true;

    final Handler wFragmentHandler = new Handler();

    public DFragment() {
        // Required empty public constructor
    }

    public static DFragment newInstance(String log) {
        DFragment fragment = new DFragment();
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
        View view = inflater.inflate(R.layout.d_fragment, container, false);
        //refreshData();
        listView = (ListView) view.findViewById(R.id.tasks);


        //UZUPELNIENIE DANYCH USERA
        nameToChange = (TextView) view.findViewById(R.id.driver_name);
        surnameToChange = (TextView) view.findViewById(R.id.driver_surname);

        setDriverInfo(login);
        fillActiveTasks();

        settingsButton = (Button) view.findViewById(R.id.settings_button_manager_main);
        settingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                showSettingsMenu(v);
            }
        });

        //klikanie tasku

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = listView.getItemAtPosition(position);
                String[] parts = o.toString().split(" ");
                Intent popUpIntent = new Intent(getActivity(), Pop.class);
                popUpIntent.putExtra("Details", o.toString());
                popUpIntent.putExtra("Type", "Driver");
                popUpIntent.putExtra("State", DatabaseHandler.getTaskState(parts[1]));
                startActivity(popUpIntent);
            }
        });

        return view;
    }

    public void fillActiveTasks() {
        orders = DatabaseHandler.getActiveTasks(SessionController.getAccountType(), SessionController.getPeselNumber());
        if(orders.size()>0) {
            String[] partsOfOrder = orders.get(0).split(" ");
            orderNum = partsOfOrder[1];
        }
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,orders);
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

   /* private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*/

   /* public void refreshData()
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


                            if(isNetworkAvailable()) {
                                fillActiveTasks();
                            }
                            else
                            {
                                message = new AlertDialog.Builder(getContext()).create();
                                message.setTitle("Connection fail");
                                message.setMessage("Internet connection fail! Check your connection.");
                                message.show();
                            }

                        }
                    });


                }

            }
        }).start();
    }*/

    public void setDriverInfo(String login){

        String info[] = DatabaseHandler.getWorkerInfo(login,"Driver");
        if(info.length>0)
        {
            //SessionController.setPeselNumber(info[0]);
            nameToChange.setText(info[1]);
            surnameToChange.setText(info[2]);
            //sessSessionController.setAccountType("Manager");
        }
    }

    private void showSettingsMenu(View v){

        settingsMenu = new PopupMenu(this.getContext(), v);
        settingsMenu.getMenuInflater().inflate(R.menu.settings_menu_driver, settingsMenu.getMenu());

        settingsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.menu1:
                        refreshing=false;
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

}
