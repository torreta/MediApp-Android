package com.mediapp.ft.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.mediapp.ft.db.DatabaseContract;
import com.mediapp.ft.db.Treatment;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 3/29/2015.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SyncAdapter";

    private static final String URL = "http://192.168.0.100:3000/api/v1/treatments";

    /**
     * Network connection timeout, in milliseconds.
     */
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    private final AccountManager mAccountManager;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        try {

            String authToken = mAccountManager.peekAuthToken(account, AccountGeneral.ACCOUNT_TYPE);
            String userObjectId = mAccountManager.getUserData(account,
                    AccountGeneral.USERDATA_USER_OBJ_ID);

            System.out.println("***INIT Get REMOTE Treatments");
            //Get Treatments from server
            List<Treatment> remoteTreatments= getTreatments(authToken);
            Log.d("MediApp", "Remote Treatment: "+remoteTreatments.toString());
            System.out.println("****FINISH Get REMOTE Treatments");

           System.out.println("****INIT Get local Treatments");
           // Get local db Treatments
           ArrayList<Treatment> mTreaments = new ArrayList<Treatment>();
           Cursor curTreatments = provider.query(DatabaseContract.Treatments.CONTENT_URI, null, null, null, null);
           if (curTreatments != null) {
               while (curTreatments.moveToNext()) {
                   mTreaments.add(Treatment.fromCursor(curTreatments));
                   Log.d("MediApp", "Local Treatment: "+Treatment.fromCursor(curTreatments).toString());
               }
               curTreatments.close();
           }
            System.out.println("****FINISH Get local Treatments");

            System.out.println("****INIT Check Missing in Remote");
            // Check Treatments missing in remote
            ArrayList<Treatment> tToRemote= new ArrayList<Treatment>();
            for (Treatment localTreatment : mTreaments) {
                if (!remoteTreatments.contains(localTreatment)) {
                    Log.d("MediApp", "Missing Treatment: "+localTreatment.toString());
                    tToRemote.add(localTreatment);
                }
            }
            System.out.println("****FINISH Check Missing in Remote");

            System.out.println("****INIT Check Missing in Local");
            //  Check Treatments missing in Local DB
            ArrayList<Treatment> tToLocal = new ArrayList<Treatment>();
            for (Treatment remoteTreatment : remoteTreatments) {
                if (!mTreaments.contains(remoteTreatment)) {
                    tToLocal.add(remoteTreatment);
                    System.out.println("****is Missing!");
                }
            }
            System.out.println("****FINISH Check Missing in Local");


            System.out.println("****INIT Update Server");
            // Update Server
            if (tToRemote.size() == 0) {
                Log.d("MediApp", "> No local changes to update server");
            } else {
                Log.d("MediApp", "> Updating remote server with local changes");

                // Updating remote tv shows
                for (Treatment remoteTreament : tToRemote) {
                    Log.d("MediApp", "> Local -> Remote [" + remoteTreament.name + "]");
                    putTreatment(authToken, userObjectId, remoteTreament);
                }
            }
            System.out.println("****FINISH Update Server");

            System.out.println("****INIT Update Local");
            // Update LocalDB
            if (tToLocal.size() == 0) {
                Log.d("MediApp", "> No local changes to update local");
            } else {
                Log.d("MediApp", "> Updating local database with remote changes");

                // Updating local treatments
                int i = 0;
                ContentValues tToLocalValues[] = new ContentValues[tToLocal.size()];
                for (Treatment localTreatment : tToLocal) {
                    Log.d("MediApp", "> Remote -> Local [" + localTreatment.name + "]");
                    tToLocalValues[i++] = localTreatment.getContentValues();
                }
                provider.bulkInsert(DatabaseContract.Treatments.CONTENT_URI, tToLocalValues);
            }
            System.out.println("****Finish Update Local");

            Log.d("MediApp", "> Finished.");

        } catch (OperationCanceledException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }


    // Get list of treatments from server
    public List<Treatment> getTreatments(String auth) throws Exception {

        List<Treatment> results =new ArrayList<Treatment>();
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(URL);
        HttpResponse response;
        String responseString = "";

        get.setHeader("token", auth);

        try {
            response = client.execute(get);
            StatusLine statusLine = response.getStatusLine();

            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();

                // Create array list of treatments from from json array
                JSONArray mJson= new JSONArray(responseString);

                //Iterate array and create new treatment. ADd treatment to list and return list
                for(int i=0;i< mJson.length(); i++){
                    JSONObject t =  mJson.getJSONObject(i);
                    Treatment mTreament= new Treatment(t.getString("name"),t.getString("start"), t.getString("finish"), t.getString("hour"),Integer.parseInt(t.getString("frequency")),"");
                    results.add(mTreament);
                }

                return results;
            }else{
                throw new Exception("Error retrieving treatments");
            }
        } catch (ClientProtocolException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }

        return results;
    }

    //Update Treatment
    public void putTreatment(String authtoken, String userId, Treatment tToAdd) throws Exception {

        Log.d("MediApp", "putTreament ["+tToAdd.name+"]");

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL);
        HttpResponse response;
        String responseString = "";

        // Parametros
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("medication_name", tToAdd.name));
        pairs.add(new BasicNameValuePair("frequency", Integer.toString(tToAdd.frequency)));
        pairs.add(new BasicNameValuePair("finish", tToAdd.finish));
        pairs.add(new BasicNameValuePair("hour", tToAdd.hour));
        pairs.add(new BasicNameValuePair("start", tToAdd.start));
        post.setEntity(new UrlEncodedFormEntity(pairs));

        // Headers
        post.setHeader("token", authtoken);

        try {

            //Ejecutar Solicitud
            response = client.execute(post);

            // Obtener codigo de respuesta
            StatusLine statusLine = response.getStatusLine();

            if(statusLine.getStatusCode() != HttpStatus.SC_CREATED){
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (ClientProtocolException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }


}
