package com.example.projectautoodo;

public class Trip
{
    //Private data
    private int mId;
    private String mMiles;
    private String mStartLocation;
    private String mDate;
    private String mEndLocation;

    //Public constructor
    public  Trip(int id, String miles, String startLocation, String endLocation, String date)
    {
        mId = id;
        mMiles = miles;
        mStartLocation = startLocation;
        mDate = date;
        mEndLocation = endLocation;
    }

    //Return string for database
    @Override
    public String toString() {
        return
                "Miles = " + mMiles  +
                ", Start Location = " + mStartLocation +
                ", End Location = " + mEndLocation +
                ", Date = " + mDate;
    }

    //Getters
    public int getmId()
    {
        return mId;
    }

    public String getmMiles()
    {
        return mMiles;
    }

    public String getmStartLocation()
    {
        return mStartLocation;
    }
    public String getmEndLocation() {return mEndLocation; }

    public String getmDate()
    {
        return mDate;
    }

    //Setters
    public void setmId(int id)
    {
        this.mId = id;
    }

    public void setmMiles(String miles)
    {
        this.mMiles = miles;
    }

    public void setmStartLocation(String startLocation)
    {
        this.mStartLocation = startLocation;
    }
    public void setmEndLocation(String endLocation) { this.mEndLocation = endLocation; }

    public void setmDate(String date)
    {
        mDate = date;
    }




}
