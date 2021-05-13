package com.example.projectautoodo;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Records extends Fragment {

    //Design
    private int sortType;
    ToggleButton toggleButton;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.records, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sortType = 0;

        view.findViewById(R.id.btn_returnRecords).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Records.this)
                        .navigate(R.id.action_Records_to_FirstFragment);
            }
        });

        ListView recordsList =(ListView) view.findViewById(R.id.listOfRecords);

        //View all records section
        TextView totalMiles = (TextView) view.findViewById(R.id.totalMiles);
        Button button = (Button) view.findViewById(R.id.btn_viewAll);
        Database db = new Database(getActivity());
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                List<Trip> allTrips = null;

                ++sortType;
                if(sortType > 2) sortType = 0;

                switch(sortType)
                {
                    case 0:
                        button.setText("Sorted By Start Location");
                        allTrips = db.allRecords(0);
                        break;
                    case 1:
                        button.setText("Sorted By Miles");
                        allTrips = db.allRecords(1);
                        break;
                    case 2:
                        button.setText("Sorted By Date");
                        allTrips = db.allRecords(2);
                        break;
                }

                //Starts database from fragment
                ArrayAdapter tripArrayAdapter = new ArrayAdapter<Trip>(getActivity(), android.R.layout.simple_list_item_1,
                                                                            allTrips);
                recordsList.setAdapter(tripArrayAdapter);

                double miles = db.getTotalMiles();
                totalMiles.setText("Total miles: " + miles);
            }
        });

        //Delete Record with Dialog box
        recordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Deleting Selected Record")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Trip clickedTrip = (Trip) parent.getItemAtPosition(position);
                                db.deleteOne(clickedTrip);
                                ShowTripOnListView(db,recordsList);
                                Toast.makeText(getActivity().getApplicationContext(), "Your information has been deleted!" , Toast.LENGTH_SHORT).show();

                                //Update the total miles.
                                double miles = db.getTotalMiles();
                                totalMiles.setText("Total miles: " + miles);
                            }

                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                builder.create().show();
            }
        });
    }

    private void ShowTripOnListView(Database db, ListView recordsList){
       ArrayAdapter tripArrayAdapter = new ArrayAdapter<Trip>(getActivity(), android.R.layout.simple_list_item_1,db.allRecords(0));
       recordsList.setAdapter(tripArrayAdapter);
    }

    //Detect Sort button is clicked.
    /*public void onToggle(View view)
    {
        ++sortType;
        if(sortType > 2)
        {
            sortType = 0;
        }

        switch(sortType)
        {
            case 0:

        }
    }*/

}