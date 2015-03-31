package com.mediapp.ft.mediapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mediapp.ft.db.Treatment;
import com.mediapp.ft.sync.SyncAdapter;

import java.util.List;

public class MainActivity extends ActionBarActivity {

    private String TAG = this.getClass().getSimpleName();
    private AccountManager mAccountManager;
    private String authToken = null;
    private Account mConnectedAccount;
    private SyncAdapter adapter = new SyncAdapter(getBaseContext(),true);

    // UI
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI
        mAccountManager = AccountManager.get(this);
        mListView = (ListView) findViewById(R.id.list_treatments);

        // Sync Button
        findViewById(R.id.btn_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, List<Treatment>>() {

                    ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                    @Override
                    protected void onPreExecute() {
                        if (authToken == null) {
                            Toast.makeText(MainActivity.this, "Please connect first", Toast.LENGTH_SHORT).show();
                            cancel(true);
                        } else {
                            progressDialog.show();
                        }
                    }

                    @Override
                    protected List<Treatment> doInBackground(Void... nothing) {
                        try {
                            return adapter.getTreatments(authToken);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(List<Treatment> treatments) {
                        progressDialog.dismiss();
                        if (treatments != null) {
                            ArrayAdapter<Treatment> adapter = new ArrayAdapter<Treatment>(MainActivity.this,
                                    android.R.layout.simple_list_item_1, treatments);
                            mListView.setAdapter(adapter);
                        }
                    }
                }.execute();
            }
        });


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

    /** Called when the user clicks the login button */
    public void goLogin(View view) {
        Intent myIntent = new Intent(MainActivity.this,LoginActivity.class);
        MainActivity.this.startActivity(myIntent);
        // Do something in response to button
    }

}
