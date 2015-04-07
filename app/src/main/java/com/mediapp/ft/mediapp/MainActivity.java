package com.mediapp.ft.mediapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mediapp.ft.db.DatabaseContract;
import com.mediapp.ft.db.Treatment;
import com.mediapp.ft.sync.AccountGeneral;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private String TAG = this.getClass().getSimpleName();
    private AccountManager mAccountManager;
    private String authToken = null;
    private Account mConnectedAccount;
    private String accountName;

     // UI
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Intent
        Global global;
        global=((Global)getApplicationContext());
        accountName = global.getAccountName();

        // UI
        mListView = (ListView) findViewById(R.id.list_treatments);
        // Read data and fill list view
        List<Treatment> treatments= readFromContentProvider();



        ArrayAdapter<Treatment> adapter = new ArrayAdapter<Treatment>(MainActivity.this, android.R.layout.simple_list_item_1, treatments);

//        ListAdapter<Treatment> adapter = new TreatmentAdapter(MainActivity.this,11,treatments);

        mListView.setAdapter(adapter);

       //Set Account VariablesVariables
        mAccountManager = AccountManager.get(this);
        mConnectedAccount = new Account( accountName, AccountGeneral.ACCOUNT_TYPE);

        // Buttons
        // Sync Button
        findViewById(R.id.btn_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mConnectedAccount == null) {
                    Toast.makeText(MainActivity.this, "Please connect first", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true); // Performing a sync no matter if it's off
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true); // Performing a sync no matter if it's off
                ContentResolver.requestSync(mConnectedAccount, DatabaseContract.AUTHORITY, bundle);
                mListView.setAdapter(new ArrayAdapter<Treatment>(MainActivity.this, android.R.layout.simple_list_item_1, readFromContentProvider()));
            }
        });

        // Set Lister on list View
        mListView.setClickable(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {

                String t = mListView.getItemAtPosition(myItemInt).toString();

                Intent myIntent = new Intent(MainActivity.this,TreatmentActivity.class);
                myIntent.putExtra("treatment",t);
                MainActivity.this.startActivity(myIntent);

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

    // Go To Login
    public void goLogin(View view) {
        Intent myIntent = new Intent(MainActivity.this,LoginActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    // Go To Login
    public void goNewTreatment(View view) {
        Intent myIntent = new Intent(MainActivity.this,NewTreatmentActivity.class);
        MainActivity.this.startActivity(myIntent);
    }


    //Get Data (treatments) from content provider
    private List<Treatment> readFromContentProvider() {
        Cursor curTreatments = getContentResolver().query(DatabaseContract.Treatments.CONTENT_URI, null, null, null, null);

        ArrayList<Treatment> treatments = new ArrayList<Treatment>();

        if (curTreatments != null) {
            while (curTreatments.moveToNext())
                treatments.add(Treatment.fromCursor(curTreatments));
            curTreatments.close();
        }
        return treatments;
    }

    private void getAccount() {
        System.out.println("*****INIT GET ACCOUNT");
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, null, this, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        Bundle bnd = null;
                        try {
                            bnd = future.getResult();
                            authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                            if (authToken != null) {

                                String accountName = bnd.getString(AccountManager.KEY_ACCOUNT_NAME);
                                mConnectedAccount = new Account(accountName, AccountGeneral.ACCOUNT_TYPE);
                            }
                            System.out.println("*****GetTokenForAccount Bundle is \" + bnd");

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("*****Error: " + e.getMessage());
                        }
                    }
                }
                , null);
        System.out.println("*****FINISH GET ACCOUNT");
    }

}
