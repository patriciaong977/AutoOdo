package com.example.projectautoodo;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.List;

public class SecondFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_returnTWM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }


        });

        //Autocomplete search section
        EditText start = (EditText) getActivity().findViewById(R.id.start_locations);
        EditText end = (EditText) getActivity().findViewById(R.id.end_location);
        EditText inputWorkMiles = (EditText) getActivity().findViewById(R.id.et_inputWM);

        final LatLng[] p1 = {null};
        final LatLng[] p2 = {null};

        AutocompleteSupportFragment startLocation = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        startLocation.setLocationBias(RectangularBounds.newInstance(
                new LatLng(-40,-168),
                new LatLng(71,136)));

        startLocation.setCountries("USA");

        startLocation.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME));
        startLocation.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place)
            {

                String startString = start.getText().toString();

                Geocoder coder = new Geocoder(getActivity());
                List<Address> address;

                if(startString.trim().equals(""))
                {
                    start.setText(place.getName());
                    String firstLocation= start.getText().toString();
                    // May throw an IOException
                    try {
                        address = coder.getFromLocationName(firstLocation, 5);
                        Address location = address.get(0);
                        p1[0] = new LatLng(location.getLatitude(), location.getLongitude() );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    end.setText(place.getName());


                    try {
                        String endString = end.getText().toString();
                        address = coder.getFromLocationName(endString, 5);
                        Address location = address.get(0);
                        p2[0] = new LatLng(location.getLatitude(), location.getLongitude() );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(!start.getText().toString().isEmpty() && !end.getText().toString().isEmpty())
                {
                    Log.d("test","works");
                  String distance = String.valueOf(SphericalUtil.computeDistanceBetween(p1[0],p2[0])/1609);
                  inputWorkMiles.setText(distance);

                }

            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

        //Stores date so we can use it later when we submit data into database
        final String[] time = {""};

        //Gets current date from calendar
        CalendarView mCalendarView;
        mCalendarView = (CalendarView) view.findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //Month starts at 0
                month = month+1;

                String date = String.format("%d/%02d/%02d", year, month, dayOfMonth);
                //String date =  year + "/" + month + "/" + dayOfMonth;
                time[0] = date;
                Log.d("date",date);
            }
        });










        //submit button  section
        EditText submit = (EditText) getActivity().findViewById(R.id.et_inputWM);


        Button button = (Button) view.findViewById(R.id.btn_submitTWM);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                //String to store user info to submit
                String startlocation = start.getText().toString();
                String endlocation = end.getText().toString();
                String miles = submit.getText().toString();

                //if user did not change date, puts current date into time[0]
                if(time[0].equals(""))
                {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    LocalDateTime now = LocalDateTime.now();
                    String date = dtf.format(now).toString();

                    time[0]= date.toString();
                }

                //starts database from fragment
                Database db = new Database(getActivity());
                boolean test = db.addOne(miles,startlocation,endlocation,time[0]);
                //clears text field
                submit.getText().clear();
                start.getText().clear();
                end.getText().clear();

                //toast message on submit
                Toast.makeText(getActivity().getApplicationContext(), "Your information has been submitted!" , Toast.LENGTH_LONG).show();

            }
        });









    }


}