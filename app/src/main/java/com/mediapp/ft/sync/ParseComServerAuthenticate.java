package com.mediapp.ft.sync;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 3/28/2015.
 */
public class ParseComServerAuthenticate implements ServerAuthenticate{

    @Override
    public String userSignUp(String userName, String email, String pass, String passConfirmation) throws Exception {

        String url = "http://torreta-163528.sae1.nitrousbox.com/api/v1/users";

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        HttpResponse response = null;

        // SignUp Data
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("name", userName));
        pairs.add(new BasicNameValuePair("email", email));
        pairs.add(new BasicNameValuePair("password", pass));
        pairs.add(new BasicNameValuePair("password_confirmation",  passConfirmation));
        httpPost.setEntity(new UrlEncodedFormEntity(pairs));


        try {
            response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() != 201) {
                throw new Exception("Error creating user");
            }

            return responseString;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return String.valueOf(response.getStatusLine().getStatusCode());
        }


    }

    @Override
    public String userSignIn(String user, String pass, String authType) throws Exception {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "http://torreta-163528.sae1.nitrousbox.com/api/v1/sessions";
        String responseString = null;

        HttpPost post = new HttpPost(url);
        post.setHeader("email", user);
        post.setHeader("password", pass);

        String authtoken="";

        try {

            HttpResponse response = httpClient.execute(post);
            StatusLine statusLine = response.getStatusLine();

            if(statusLine.getStatusCode() != HttpStatus.SC_CREATED){
                throw new Exception("Error signing-in");
            }

            // Get response String
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            responseString = out.toString();

            // Get Token String
            JSONObject myjson = new JSONObject(responseString);
            String token = myjson.getString("token");

            // Get User Info
            myjson = new JSONObject(myjson.getString("user"));
            String email = myjson.getString("email");

            //Set new User
            User loggedUser = new User(email,token);
            authtoken = loggedUser.sessionToken;
        } catch (IOException e) {
            e.getMessage();
        }

        return authtoken;
    }


    private class ParseComError implements Serializable {
        int code;
        String error;
    }
    private class User implements Serializable {

        private String email;
        public String sessionToken;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public User(String email, String sessionToken){
            this.email = email;
            this.sessionToken = sessionToken;
        }

        public String getSessionToken() {
            return sessionToken;
        }

        public void setSessionToken(String sessionToken) {
            this.sessionToken = sessionToken;
        }

    }
}