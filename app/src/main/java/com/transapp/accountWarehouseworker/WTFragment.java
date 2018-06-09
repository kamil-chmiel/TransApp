package com.transapp.accountWarehouseworker;

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
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.databaseHandler.DatabaseHandler;
import com.transapp.R;

import java.util.ArrayList;

public class WTFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";


    private OnFragmentInteractionListener mListener;
    private Button addItemButton, addNewItemButton, deleteItemButton;
    private ArrayList<String> items = new ArrayList<String>();
    private Spinner itemsSpinner, deleteItemsSpinner;
    private EditText amountText, nameText, priceText, dimensionsText, weightText, newAmountText;

    private enum States {
        NAME,PRICE, PRICE_WRONG_FORMAT,DIMENSIONS, DIMENSIONS_WRONG_FORMAT,WEIGHT, WEIGHT_WRONG_FORMAT,AMOUNT,OK
    }

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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.w_t_fragment, container, false);
        addItemButton =  view.findViewById(R.id.add_button);
        addNewItemButton = view.findViewById(R.id.add_new_item_button);
        deleteItemButton =  view.findViewById(R.id.delete_button);
        itemsSpinner =  view.findViewById(R.id.items_spinner);
        deleteItemsSpinner =  view.findViewById(R.id.delete_items_spinner);
        TabHost tab =  view.findViewById(R.id.tabHost);
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


        // DODAWANIE ILOŚĆI PRZEDMIOTÓW DO BAZY

        amountText = view.findViewById(R.id.amount_text);
        fillAvailableItems();

        addItemButton.setOnClickListener((View v) ->
        {

            String temp = itemsSpinner.getSelectedItem().toString();
            String arr[] = temp.split(" ");

           if(amountText.getText().toString().matches("|0"))
               Toast.makeText(this.getContext(), "Please state the amount !", Toast.LENGTH_LONG).show();
           else {

               DatabaseHandler.addItem(Integer.parseInt(arr[0]), Integer.parseInt(amountText.getText().toString()));

               String product = temp.replaceAll("Ilosc:\\s?([-])?\\d+","")
                                    .replaceAll("\\d+\\s?[)]\\s?","");

               Snackbar.make(getActivity().findViewById(R.id.w_t_linearlayout),product + ": amount increased by: " + amountText.getText().toString(), Snackbar.LENGTH_LONG).show();

               amountText.setText("");
               fillAvailableItems();
           }


        });


        // OBSLUGA DODANIA NOWEGO TOWARU

        nameText =  view.findViewById(R.id.name_text);
        priceText =  view.findViewById(R.id.price_text);
        dimensionsText =  view.findViewById(R.id.dimensions_text);
        weightText =  view.findViewById(R.id.weight_text);
        newAmountText = view.findViewById(R.id.new_amount_text);


        addNewItemButton.setOnClickListener((View v) ->
        {
            States state = checkFieldStatesAddItem();

            switch(state){

                case NAME:
                    Toast.makeText(this.getContext(), "Please state the name of product!", Toast.LENGTH_LONG).show();
                    break;

                case PRICE:
                    Toast.makeText(this.getContext(), "Please state the price of product!", Toast.LENGTH_LONG).show();
                    break;

                case PRICE_WRONG_FORMAT:
                    Toast.makeText(this.getContext(), "Price of product must be in (numbers).xx format !", Toast.LENGTH_LONG).show();
                    break;

                case DIMENSIONS:
                    Toast.makeText(this.getContext(), "Please state the dimensions of product!", Toast.LENGTH_LONG).show();
                    break;

                case DIMENSIONS_WRONG_FORMAT:
                    Toast.makeText(this.getContext(), "Dimensions of product must be in (numbers)x(numbers)x(numbers) format !", Toast.LENGTH_LONG).show();
                    break;

                case WEIGHT:
                    Toast.makeText(this.getContext(), "Please state the weight of product!", Toast.LENGTH_LONG).show();
                    break;

                case WEIGHT_WRONG_FORMAT:
                    Toast.makeText(this.getContext(), "Weight of product must be in (numbers).xx format !", Toast.LENGTH_LONG).show();
                    break;

                case AMOUNT:
                    Toast.makeText(this.getContext(), "Please state the amount of product!", Toast.LENGTH_LONG).show();
                    break;

                case OK:

                    if(!isInDatabase(nameText.getText().toString().trim())) {

                        DatabaseHandler.addNewItem(nameText.getText().toString(),
                                Float.parseFloat(priceText.getText().toString()),
                                dimensionsText.getText().toString(),
                                Float.parseFloat(weightText.getText().toString()),
                                Integer.parseInt(newAmountText.getText().toString()));

                        Snackbar.make(getActivity().findViewById(R.id.w_t_linearlayout),nameText.getText().toString() + " added to database !", Snackbar.LENGTH_LONG).show();

                        nameText.setText("");
                        priceText.setText("");
                        dimensionsText.setText("");
                        weightText.setText("");
                        newAmountText.setText("");
                        fillAvailableItems();
                    }
                    else
                        Toast.makeText(this.getContext(), "Item is already existing in the database !", Toast.LENGTH_LONG).show();

                    break;

            }

        });

        // USUNIECIE TOWARU

        deleteItemButton.setOnClickListener((View v) ->
        {
                    String temp = deleteItemsSpinner.getSelectedItem().toString();
                    String arr[] = temp.split(" ");
                    DatabaseHandler.deleteItem(Integer.parseInt(arr[0]));

                    String product = temp.replaceAll("Ilosc:\\s?([-])?\\d+","")
                                         .replaceAll("\\d+\\s?[)]\\s?","");

                    Snackbar.make(getActivity().findViewById(R.id.w_t_linearlayout),product + ": deleted from database !", Snackbar.LENGTH_LONG).show();

                    fillAvailableItems();


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

    private States checkFieldStatesAddItem(){

        States state = States.OK;


        if(newAmountText.getText().toString().matches(""))
            state = States.AMOUNT;

        if(weightText.getText().toString().matches(""))
            state = States.WEIGHT;
        else if(!weightText.getText().toString().matches("\\d+\\.\\d{2}\\s*"))
            state = States.WEIGHT_WRONG_FORMAT;

        if(dimensionsText.getText().toString().matches(""))
            state = States.DIMENSIONS;
        else if(!dimensionsText.getText().toString().matches("\\d+[x]\\d+[x]\\d+\\s*"))
            state = States.DIMENSIONS_WRONG_FORMAT;

        if(priceText.getText().toString().matches(""))
            state = States.PRICE;
        else if(!priceText.getText().toString().matches("\\d+\\.\\d{2}\\s*"))
            state = States.PRICE_WRONG_FORMAT;

        if(nameText.getText().toString().matches(""))
            state = States.NAME;


        return state;
    }

    private boolean isInDatabase(String name){

        String itemChecked;

        for(String item: items) {

            itemChecked = item.replaceAll("Ilosc:\\s?([-])?\\d+", "")
                    .replaceAll("\\d+\\s?[)]\\s?", "")
                    .trim();

            if(itemChecked.equals(name))
                return true;
        }

        return  false;
    }

}
