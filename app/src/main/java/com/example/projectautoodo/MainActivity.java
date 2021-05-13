package com.example.projectautoodo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.SyncStateContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class MainActivity extends AppCompatActivity
{

    //References to buttons and other controls
    Button btn_TWM, btn_returnGMI, btn_submit, btn_submitTWM, btn_delete;
    CalendarView calendarView;
    EditText et_inputWM;
    ListView listOfRecords;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_TWM = findViewById(R.id.btn_TWM);
        btn_returnGMI = findViewById(R.id.btn_returnGMI);
        btn_submit = findViewById(R.id.btn_submitTWM);
        btn_submitTWM = findViewById(R.id.btn_submitTWM);


        listOfRecords = findViewById(R.id.listOfRecords);
        calendarView = findViewById(R.id.calendarView);

        et_inputWM = findViewById(R.id.et_inputWM);

        //Places api initiate
        Places.initialize(getApplicationContext(),"AIzaSyCi88d8ZyQtwTN2y07TuiwsddmKFZnDGJI");
        PlacesClient placesClient = Places.createClient(this);

        //Kingsley (this is to inititate the database, used it as a test)
       // Database db = new Database(MainActivity.this);
        //boolean test = db.addOne("1","2");

    }

    //Create Dialog when Back Button is Pressed.
    @Override
    public void onBackPressed()
    {
        createDialog();
    }

    private void createDialog()
    {
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setMessage("Are you sure you want to return?");
        alertDlg.setCancelable(false);

        alertDlg.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                MainActivity.super.onBackPressed();
            }
        });

        alertDlg.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        alertDlg.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}