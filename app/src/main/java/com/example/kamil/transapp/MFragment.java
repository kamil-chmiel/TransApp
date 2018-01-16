package com.example.kamil.transapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import java.util.ArrayList;

public class MFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private OnFragmentInteractionListener mListener;
    static String login;
    private FloatingActionButton addUserButton;
    private FloatingActionButton removeUserButton;
    private Button settingsButton;
    private static PopupMenu settingsMenu;
    ListView listView;
    ArrayAdapter<String> adapter;
    TextView nameToChange, surnameToChange;
    ArrayList<String> orders;
    private static boolean refreshing = true;


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
        refreshData();
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

        // PODPIECIE BUTTONOW
       /* addUserButton = view.findViewById(R.id.addUserButton);
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

    private void fillActiveTasks() {
        orders = DatabaseHandler.getActiveTasks(SessionController.getAccountType(), SessionController.getPeselNumber());
        if(orders!=null) {
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, orders);
            listView.setAdapter(adapter);
        }
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
