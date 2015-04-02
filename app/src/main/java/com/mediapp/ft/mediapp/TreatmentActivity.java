package com.mediapp.ft.mediapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mediapp.ft.db.DatabaseContract;
import com.mediapp.ft.db.DbHelper;

import org.json.JSONObject;


public class TreatmentActivity extends ActionBarActivity {

    private String treatment;

    // UI
    private TextView mTreatmentView;
    private Button mBtnDeletetView;

    //Treatment
    private String name;
    private String start;
    private String finish;
    private String hour;
    private String frequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);

        //Get Treatment
        treatment = getIntent().getStringExtra("treatment");

        //UI
        mTreatmentView = (TextView) findViewById(R.id.treatment);
        mBtnDeletetView = (Button) findViewById(R.id.btn_delete);

        //Set Treatment Text
        mTreatmentView.setText(treatment);

        //Set Treatment values
        try {
            JSONObject json = new JSONObject(treatment);
            name = json.getString("name");
            start  =json.getString("start");
            finish =json.getString("finish");
            hour =json.getString("hour");
            frequency =json.getString("frequency");
        } catch (Exception e) {
            e.printStackTrace();
            name =  null;
            start  = null;
            finish = null;
            hour = null;
            frequency = null;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_treatment, menu);
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

    public void deleteTreatment(View view) {

        DbHelper dbHelper= new DbHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = DatabaseContract.Treatments.COLUMN_NAME_NAME + "='" + name+"' AND "
                + DatabaseContract.Treatments.COLUMN_NAME_START + "='" + start+"' AND "
                + DatabaseContract.Treatments.COLUMN_NAME_FINISH + "='" + finish+"' AND "
                + DatabaseContract.Treatments.COLUMN_NAME_HOUR + "='" + hour+"' AND "
                + DatabaseContract.Treatments.COLUMN_NAME_FREQUENCY + "='" + frequency+"'";

        try {
            db.delete(DatabaseContract.Treatments.TABLE_NAME, whereClause, null);
            mBtnDeletetView.setEnabled(false);
            Toast.makeText(TreatmentActivity.this, "Treatment deleted!", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.d("MediApp",e.getMessage());
            e.printStackTrace();
        }
    }

    public void goBack(View view) {
        Intent myIntent = new Intent(TreatmentActivity.this,MainActivity.class);
        TreatmentActivity.this.startActivity(myIntent);
    }
}
