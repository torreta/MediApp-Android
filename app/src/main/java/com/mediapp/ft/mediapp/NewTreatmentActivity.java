package com.mediapp.ft.mediapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import com.mediapp.ft.db.DatabaseContract;
import com.mediapp.ft.db.DbHelper;
import com.mediapp.ft.db.Treatment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


//TODO: dates and time formats
public class NewTreatmentActivity extends ActionBarActivity {

    // UI references.
    private EditText mNameView;
    private EditText mFinishView;
    private EditText mHourView;
    private EditText mFrequencyView;
    private View mTreatmentFormView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_treatment);

        // Set Up UI References
        mNameView = (EditText) findViewById(R.id.t_name);
        mFinishView = (EditText) findViewById(R.id.t_finish);
        mHourView = (EditText) findViewById(R.id.t_hour);
        mFrequencyView = (EditText) findViewById(R.id.t_frequency);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_treatment, menu);
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

    // Insert new Treatment in local DB
    public void addTreatment(View view) {

        // Check form input data and set errors
        if (!isValidForm()) return;

        // Get Current Date
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String start = df.format(Calendar.getInstance().getTime());
        Log.d("MediApp", "Start Date"+ start);

        try {
            // Create New Object Treatment
            Treatment t = new Treatment(mNameView.getText().toString(), start, mFinishView.getText().toString(), mHourView.getText().toString(), Integer.parseInt(mFrequencyView.getText().toString()), "");
            Log.d("MediApp", "New Treatment "+t.toString());

            // Treatment Content Values
            ContentValues tToLocalValues = new ContentValues();
            tToLocalValues = t.getContentValues();

            Log.d("MediApp", "Content Values "+tToLocalValues.toString());

            //Insert in Local DB database
            DbHelper dbHelper= new DbHelper(getBaseContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert(DatabaseContract.Treatments.TABLE_NAME,null,tToLocalValues);
            Log.d("MediApp", "Inserted ");

            //creating the notification
            Intent intent = new Intent();
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
            Notification noti = new Notification.Builder(this)
                    .setTicker("Take Treatment")
                    .setContentTitle(mNameView.getText().toString()) //name
                    .setContentText("Take your pill at: " + mHourView.getText().toString())
                    .setSmallIcon(R.drawable.pill)
                    .setContentIntent(pIntent).getNotification();
            noti.flags=Notification.FLAG_AUTO_CANCEL;
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, noti);

        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(NewTreatmentActivity.this, "Couldn't add new treatment. Try again!", Toast.LENGTH_SHORT).show();
            Log.d("MediApp", e.getMessage());
            return;
        }

        //Show Toast
        Toast.makeText(NewTreatmentActivity.this, "New treatment added!", Toast.LENGTH_SHORT).show();

        //Clear Forms
        mNameView.getText().clear();
        mFinishView.getText().clear();
        mHourView.getText().clear();
        mFrequencyView.getText().clear();

    }

    public boolean isValidForm(){

        // Reset Errors
        mNameView.setError(null);
        mFinishView.setError(null);
        mHourView.setError(null);
        mFrequencyView.setError(null);

        // Store values at the time of the login attempt.
        String name = mNameView.getText().toString();
        String finish = mFinishView.getText().toString();
        String hour = mHourView.getText().toString();
        String frequency = mFrequencyView.getText().toString();

        // Check if Empty
        if(TextUtils.isEmpty(name)){
            mNameView.setError("Medication name is empty");
            return false;
        }
        if(TextUtils.isEmpty(finish)){
            mFinishView.setError("Medication finish date is empty");
            return false;
        }
        if(TextUtils.isEmpty(hour)){
            mHourView.setError("Medication hour is empty");
            return false;
        }
        if(TextUtils.isEmpty(frequency)){
            mFrequencyView.setError("Medication hours frequency is empty");
            return false;
        }

        // Check if finish is a date
        if(!AppUtils.isValidDate(finish,"yyyy-MM-dd")){
            mFinishView.setError("Invalid date");
            return false;
        }

        // Check if Hour is and hour HH:mm
        if(!AppUtils.isValidDate(hour,"HH:mm")){
            mHourView.setError("Invalid hour");
            return false;
        }

        //Check if frequency is a valid number
        if(!AppUtils.isValidNumber(frequency)){
            mFrequencyView.setError("Invalid hours frequency");
            return false;
        }

        return true;
    }

    public void goMain(View view) {
        Intent myIntent = new Intent(NewTreatmentActivity.this, MainActivity.class);
        NewTreatmentActivity.this.startActivity(myIntent);
    }
}
