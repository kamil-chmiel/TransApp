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
    private Button addItemButton, addNewItemButton, deleteItemButton;
    private ArrayList<String> items = new ArrayList<String>();
    private boolean readData = true;
    private String orderNum;
    private ArrayList<Task> tasks;
    private Spinner itemsSpinner, deleteItemsSpinner;
    private EditText amountText, nameText, priceText, dimensionsText, weightText, newAmountText;
    private int itemToChange;
    private int amount;

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
        addItemButton = (Button) view.findViewById(R.id.add_button);
        addNewItemButton = (Button) view.findViewById(R.id.add_new_item_button);
        deleteItemButton = (Button) view.findViewById(R.id.delete_button);
        itemsSpinner = (Spinner) view.findViewById(R.id.items_spinner);
        deleteItemsSpinner = (Spinner) view.findViewById(R.id.delete_items_spinner);
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


        amountText = (EditText) view.findViewById(R.id.amount_text);
        fillAvailableItems();

        addItemButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    String temp = itemsSpinner.getSelectedItem().toString();
                    String arr[] = temp.split(" ");
                    if(amountText.getText()!=null)
                        DatabaseHandler.addItem(Integer.parseInt(arr[0]) ,Integer.parseInt(amountText.getText().toString()));
                    amountText.setText("");
                }
                catch(Exception e)
                {
                    AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                    Message.setTitle("Error");
                    Message.setMessage("Amount '"+nameText.getText()+"' hasn't been added.");
                    Message.show();
                }
                finally
                {
                    AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                    Message.setTitle("Amount added!");
                    Message.setMessage("Amount has been added to the base!");
                    Message.show();
                }
            }
        });


        // OBSLUGA DODANIA NOWEGO TOWARU

        nameText = (EditText) view.findViewById(R.id.name_text);
        priceText = (EditText) view.findViewById(R.id.price_text);
        dimensionsText = (EditText) view.findViewById(R.id.dimensions_text);
        weightText = (EditText) view.findViewById(R.id.weight_text);
        newAmountText = (EditText) view.findViewById(R.id.new_amount_text);

        addNewItemButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    if (nameText.getText() != null && priceText != null && dimensionsText != null && weightText != null && newAmountText != null) {
                        DatabaseHandler.addNewItem(nameText.getText().toString(), Float.parseFloat(priceText.getText().toString()),
                                dimensionsText.getText().toString(), Float.parseFloat(weightText.getText().toString()),
                                Integer.parseInt(newAmountText.getText().toString()));

                        nameText.setText("");
                        priceText.setText("");
                        dimensionsText.setText("");
                        weightText.setText("");
                        newAmountText.setText("");
                    }
                }
                catch(Exception e)
                {
                    AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                    Message.setTitle("Error");
                    Message.setMessage("Item '"+nameText.getText()+"' hasn't been added.");
                    Message.show();
                }
                finally
                {
                    AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                    Message.setTitle("New item added!");
                    Message.setMessage("Item '"+nameText.getText()+"' has been added to the base!");
                    Message.show();
                }
            }
        });

        // USUNIECIE TOWARU

        deleteItemButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    String temp = deleteItemsSpinner.getSelectedItem().toString();
                    String arr[] = temp.split(" ");
                    DatabaseHandler.deleteItem(Integer.parseInt(arr[0]));
                    fillAvailableItems();
                }
                catch(Exception e)
                {
                    AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                    Message.setTitle("Error");
                    Message.setMessage("Item hasn't been deleted.");
                    Message.show();
                }
                finally
                {
                    AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                    Message.setTitle("Item deleted!");
                    Message.setMessage("Item has been deleted from the base!");
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



    public void fillAvailableItems()
    {
        items = DatabaseHandler.getInstance().getItems();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemsSpinner.setAdapter(adapter);
        deleteItemsSpinner.setAdapter(adapter);
    }

}
