package com.example.projectautoodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class Database extends SQLiteOpenHelper
{

    //table strings
    public static final String MILES_TABLE = "MILES_TABLE";
    public static final String KEY_ID= "ID";
    public static final String COLUMN_MILES = "MILES";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_START = "STARTLOCATION";
    public static final String COLUMN_END = "ENDLOCATION";

    public Database(@Nullable Context context) {
        super(context, "workmiles.db", null,1);
    }


    //table creation
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MILES_TABLE = "CREATE TABLE " + MILES_TABLE + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MILES + " TEXT, "
                + COLUMN_START + " TEXT, "
                + COLUMN_END +  " TEXT, " + COLUMN_DATE + " DATE " + ")";
        db.execSQL(CREATE_MILES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onCreate(db);
    }

    //test method of insertion into database
    public boolean addOne(String miles, String start, String end, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_MILES, miles);
        cv.put(COLUMN_START, start);
        cv.put(COLUMN_END, end);
        cv.put(COLUMN_DATE, date);

        long insert = db.insert(MILES_TABLE, null, cv);

        if(insert== -1)
        {
            return false;
        }
        else {
            return true;
        }
    }

    public List<Trip> allRecords(int sortType)
    {
        List<Trip> returnList = new ArrayList<>();

        //Get data from the database

        String queryString = "SELECT * FROM " + MILES_TABLE;

        switch(sortType)
        {
            case 0:
                queryString += " ORDER BY " + COLUMN_START;
                break;
            case 1:
                queryString += " ORDER BY " + COLUMN_MILES;
                break;
            case 2:
                queryString += " ORDER BY " + COLUMN_DATE;
                break;
        }

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst())
        {
            do
                {
                    int milesId = cursor.getInt(0);
                    String miles = cursor.getString(1);
                    String startLocation = cursor.getString(2);
                    String endLocation = cursor.getString(3);
                    String date = cursor.getString(4);


                    Trip newTrip = new Trip(milesId,miles,startLocation,endLocation,date);
                    returnList.add(newTrip);

                }    while(cursor.moveToNext());


        }
        else
        {

        }

        //close cursor and database
        cursor.close();
        db.close();
        return returnList;

    }

   // Delete One
    public boolean deleteOne(Trip tripmodel)
    {
        //Look for workmiles model in the database. Once found, delete it and return true.
            //If not found return false
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + MILES_TABLE + " WHERE " + KEY_ID + " = " + tripmodel.getmId();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            return true;
        }
        else
        {
            return false;
        }
    }

    //Get Total Miles
    public double getTotalMiles()
    {
        String queryString = "SELECT * FROM " + MILES_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        //If cursor is empty, return 0.0.
        if(!cursor.moveToFirst()) {
            return 0.0;
        }

        double totalMiles = 0.0;

        //Add all miles entries to totalMiles.
        do {
            String mileString = cursor.getString(1);
            double mileDouble = Double.parseDouble(mileString);
            totalMiles += mileDouble;
        }while(cursor.moveToNext());

        //double totalMiles = 1234.56;

        cursor.close();
        db.close();

        return totalMiles;
    }

}
