package com.mediapp.ft.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by Frank on 3/30/2015.
 */
public class Treatment implements Serializable {

    // Fields
    public String name;
    public String start;
    public String finish;
    public String hour;
    public int frequency;
    public String picture;

    public Treatment(String name, String start, String finish, String hour, int frequency, String picture) {
        this.name = name;
        this.start = start;
        this.finish = finish;
        this.hour = hour;
        this.frequency = frequency;
        this.picture = picture;
    }

    /**
     * Convenient method to get the objects data members in ContentValues object.
     * This will be useful for Content Provider operations,
     * which use ContentValues object to represent the data.
     *
     * @return
     */
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Treatments.COLUMN_NAME_NAME, name);
        values.put(DatabaseContract.Treatments.COLUMN_NAME_START, start);
        values.put(DatabaseContract.Treatments.COLUMN_NAME_FINISH, finish);
        values.put(DatabaseContract.Treatments.COLUMN_NAME_FREQUENCY, frequency);
        values.put(DatabaseContract.Treatments.COLUMN_NAME_HOUR, hour);
        values.put(DatabaseContract.Treatments.COLUMN_NAME_PICTURE, picture);
        return values;
    }

    // Create from Cursor
    public static Treatment fromCursor(Cursor curTreatments) {
        String name = curTreatments.getString(curTreatments.getColumnIndex(DatabaseContract.Treatments.COLUMN_NAME_NAME));
        String start = curTreatments.getString(curTreatments.getColumnIndex(DatabaseContract.Treatments.COLUMN_NAME_START));
        String finish = curTreatments.getString(curTreatments.getColumnIndex(DatabaseContract.Treatments.COLUMN_NAME_FINISH));
        String hour = curTreatments.getString(curTreatments.getColumnIndex(DatabaseContract.Treatments.COLUMN_NAME_HOUR));
        int frequency = curTreatments.getInt(curTreatments.getColumnIndex(DatabaseContract.Treatments.COLUMN_NAME_FREQUENCY));
        String picture = curTreatments.getString(curTreatments.getColumnIndex(DatabaseContract.Treatments.COLUMN_NAME_PICTURE));

        return new Treatment(name, start,finish,hour,frequency,picture);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        //Cast argument to Treatment Object
        Treatment t = (Treatment) o;

        if (!name.equals(t.name) && hour.equals(t.hour) && frequency !=t.frequency) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "name='" + name + '\'' +
                ", start='" + start + '\'' +
                ", finish='" + finish + '\'' +
                ", hour='" + hour + '\'' +
                ", frequency=" + frequency +
                ", picture='" + picture + '\'' +
                '}';
    }


}
