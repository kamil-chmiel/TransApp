package com.transapp.accountManager;

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
import android.widget.TextView;
import android.widget.Toast;

import com.databaseHandler.DatabaseHandler;
import com.transapp.R;
import com.transapp.utilities.SessionController;
import com.transapp.models.Customer;
import com.transapp.models.Driver;
import com.transapp.models.Item;
import com.transapp.models.Task;
import com.transapp.models.WarehouseWorker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MTFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private OnFragmentInteractionListener mListener;
    private Button addTaskButton;
    private ArrayList<String> workers = new ArrayList<String>();
    private ArrayList<String> items = new ArrayList<String>();
    private ArrayList<Integer> itemsAmount = new ArrayList<>();
    private ArrayList<Item> itemsAmountAboveZero = new ArrayList<>();
    private String orderNum;
    private Spinner workersSpinner;
    private Spinner customerSpinner;
    private Spinner driverSpinner;
    private TextView orderTV;
    private Spinner itemsSpinner;
    private TextView itemsTV;
    private TextView description;
    private TextView deadline;
    private Button addItemButton;
    private EditText addItemAmount;

    private enum States {
        CUSTOMER,ITEMS, DESCRIPTION,DEADLINE,DEADLINE_WRONG_FORMAT,DRIVERS, WORKERS,OK
    }

    public MTFragment() {
        // Required empty public constructor
    }

    public static MTFragment newInstance(int nr) {
        MTFragment fragment = new MTFragment();
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
        View view = inflater.inflate(R.layout.m_t_fragment, container, false);
        workersSpinner =  view.findViewById(R.id.workersSpinner);
        customerSpinner =  view.findViewById(R.id.customerSpinner);
        driverSpinner =  view.findViewById(R.id.driverSpinner);
        orderTV =  view.findViewById(R.id.orderNum);
        itemsSpinner =  view.findViewById(R.id.items_to_add_spinner);
        description = view.findViewById(R.id.descriptionText);
        deadline =  view.findViewById(R.id.deadlineText);
        itemsTV =  view.findViewById(R.id.items_text);
        addItemButton = view.findViewById(R.id.add_item_button);
        addItemAmount =  view.findViewById(R.id.itemsAmount);

        orderNum = ("Order #"+(new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(Calendar.getInstance().getTime())));
        orderTV.setText(orderNum);

        fillAvailableWorkers(workersSpinner, customerSpinner, driverSpinner);
        fillAvailableItems(itemsSpinner);


        addItemButton.setOnClickListener((View v) -> {

            Integer amount = itemsAmountAboveZero.get(itemsSpinner.getSelectedItemPosition()).getAmount();

            if(Integer.parseInt(addItemAmount.getText().toString()) <= amount ) {
                itemsTV.append(" " +itemsAmountAboveZero.get(itemsSpinner.getSelectedItemPosition()).getName() + " x" + Integer.parseInt(addItemAmount.getText().toString()) + "; ");

                // decrease item amount

                itemsAmountAboveZero.get(itemsSpinner.getSelectedItemPosition()).setAmount(amount - Integer.parseInt(addItemAmount.getText().toString()));

                // reload spinner
                refreshItems(itemsSpinner);

            }
            else {
                AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                Message.setTitle("Out of items!");
                Message.setMessage("Out of items amount in storage.");
                Message.show();
            }
        });

        addTaskButton = view.findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener((View v) -> {


          States state = checkFieldStatesAddOrder();

          switch (state){

              case CUSTOMER:
                  Toast.makeText(getContext(), "No customer selected!", Toast.LENGTH_LONG).show();
                  break;
              case ITEMS:
                  Toast.makeText(getContext(), "No items added!", Toast.LENGTH_LONG).show();
                  break;
              case DESCRIPTION:
                  Toast.makeText(getContext(), "Please state description!", Toast.LENGTH_LONG).show();
                  break;
              case DEADLINE:
                  Toast.makeText(getContext(), "Please state deadline!", Toast.LENGTH_LONG).show();
                  break;
              case DEADLINE_WRONG_FORMAT:
                  Toast.makeText(getContext(), "Wrong deadline format!", Toast.LENGTH_LONG).show();
                  break;
              case DRIVERS:
                  Toast.makeText(getContext(), "No driver selected!", Toast.LENGTH_LONG).show();
                  break;
              case WORKERS:
                  Toast.makeText(getContext(), "No warehouse worker selected!", Toast.LENGTH_LONG).show();
                  break;
              case OK:

                  String pesel = customerSpinner.getSelectedItem().toString();
                  pesel = pesel.substring(0, pesel.indexOf(" "));
                  String[] customerInfo = DatabaseHandler.getWorkerDetails("klient", pesel);
                  Customer newCustomer = new Customer(customerInfo[0], customerInfo[1], customerInfo[2], customerInfo[3]);

                  pesel = workersSpinner.getSelectedItem().toString();
                  pesel = pesel.substring(0, pesel.indexOf(" "));
                  String[] workerInfo = DatabaseHandler.getWorkerDetails("pracownik_magazynu", pesel);
                  WarehouseWorker newWorker = new WarehouseWorker(workerInfo[0], workerInfo[1], workerInfo[2]);

                  pesel = driverSpinner.getSelectedItem().toString();
                  pesel = pesel.substring(0, pesel.indexOf(" "));
                  String[] driverInfo = DatabaseHandler.getWorkerDetails("kierowca", pesel);
                  Driver newDriver = new Driver(driverInfo[0], driverInfo[1], driverInfo[2]);

                  Task newTask = new Task(orderNum, itemsTV.getText().toString(),
                          description.getText().toString(), deadline.getText().toString(), newCustomer, newWorker, newDriver);

                  String messageFromBase = "";
                  try {
                      messageFromBase = newTask.sendToDataBase();
                  } catch (Exception ex) {
                      System.out.println("Bląd podczas wysylania taska do bazy " + ex.getMessage());
                  }

                  if (messageFromBase.equals("")) {
                      SessionController.addTask(newTask);
                      updateDatabase();
                      AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                      Message.setTitle("Task saved!");
                      Message.setMessage("Task #" + orderNum + " has been saved!");
                      Message.show();
                      clearForm();
                  } else {
                      AlertDialog Message = new AlertDialog.Builder(getContext()).create();
                      Message.setTitle("Fail!");
                      Message.setMessage(messageFromBase);
                      Message.show();
                  }

                  break;
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

    public void updateDatabase() {

    }

    public void clearForm()
    {
        orderNum = ("Order #"+(new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(Calendar.getInstance().getTime())));
        orderTV.setText(orderNum);
        itemsTV.setText("Items list:");
        description.setText("");
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

    public void refreshItems(Spinner itemsSpinner) {
        ArrayList<String> itemLabels = new ArrayList<>();

        for (int i=0; i<itemsAmountAboveZero.size(); i++) {
            if(itemsAmountAboveZero.get(i).getAmount() > 0) {
                itemLabels.add(itemsAmountAboveZero.get(i).getName() + " " + itemsAmountAboveZero.get(i).getAmount());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, itemLabels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemsSpinner.setAdapter(adapter);
    }

    public void fillAvailableItems(Spinner itemsSpinner)
    {
        items = DatabaseHandler.getInstance().getAvailableItems();
        itemsAmount = DatabaseHandler.getInstance().getAvailableItemsAmount();
        ArrayList<String> itemLabels = new ArrayList<>();
        for (int i=0; i<items.size(); i++) {
            if(itemsAmount.get(i)>0) {
                itemsAmountAboveZero.add( new Item(items.get(i), itemsAmount.get(i)) );
                itemLabels.add(items.get(i) + " " + itemsAmount.get(i));
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, itemLabels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemsSpinner.setAdapter(adapter);
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

    private States checkFieldStatesAddOrder(){

        States state = States.OK;

        if(driverSpinner.getSelectedItem() == null || driverSpinner.getSelectedItem().toString().matches(""))
            state = States.DRIVERS;

        if(workersSpinner.getSelectedItem() == null || workersSpinner.getSelectedItem().toString().matches(""))
            state = States.WORKERS;

        if(deadline.getText().toString().matches(""))
            state = States.DEADLINE;
        else if(!deadline.getText().toString().matches("^([0-3][0-9])-([0][1-9]|[1][0-2])-([2][0][1][8-9])"))
            state = States.DEADLINE_WRONG_FORMAT;

        if(description.getText().toString().matches(""))
            state = States.DESCRIPTION;

        if(itemsTV.getText().toString().matches("Items list:") || itemsTV.getText() == null)
            state = States.ITEMS;

        if(customerSpinner.getSelectedItem() == null || customerSpinner.getSelectedItem().toString().matches(""))
            state = States.CUSTOMER;


        return state;
    }

}
