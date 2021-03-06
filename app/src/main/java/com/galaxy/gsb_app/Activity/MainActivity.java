package com.galaxy.gsb_app.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.galaxy.gsb_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private EditText etEmail;
    private EditText etPassword;
    private JSONArray MyVisiteur;
    private CheckBox checkBoxRememberMe;
    public static String filename = "Valustoringfile";
    SharedPreferences SP;
    //private JSONArray role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Reference to variables
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);

        SP = getSharedPreferences(filename, 0);

        String getname = SP.getString("key1","");
        String getpass = SP.getString("key2","");
        Boolean getRememberMe = SP.getBoolean("key3", false);

        if (getRememberMe)
        {
            checkBoxRememberMe.setChecked(true);
        }

        etEmail.setText(getname);
        etPassword.setText(getpass);


        etEmail.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            checkLogin(v);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        etPassword.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            checkLogin(v);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

    }

    // Triggers when LOGIN Button clicked
    public void checkLogin(View arg0) {

        // Get text from email and password field
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();


        if(checkBoxRememberMe.isChecked())
        {
            SharedPreferences.Editor editit = SP.edit();
            editit.putString("key1", email);
            editit.putString("key2",password);
            editit.putBoolean("key3", true);
            editit.apply();
        }
        else
        {
            SharedPreferences.Editor editit = SP.edit();
            editit.clear();
            editit.apply();
        }


        // Initialize  AsyncLogin() class with email and password
        new AsyncLogin().execute(email,password);
    }

    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try
            {
                // Enter URL address where your php file resides
                url = new URL("http://rulliereolivier.fr/apigsb/login.php");

            }
            catch (MalformedURLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }

            try
            {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            }
            catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    Log.e("Response: ", "> " + result);

                    if (result != null) {

                        try {

                            JSONObject jsonObj = new JSONObject(String.valueOf(result));


                            String resultLog = jsonObj.getString("result");
                            MyVisiteur = jsonObj.getJSONArray("Visiteur");
                            //role = jsonObj.getJSONArray("roles");


                            return(resultLog.toString());
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            return "exception";
                        }
                    }
                    else
                    {
                        return "exception";
                    }
                }
                else
                {

                    return("unsuccessful");
                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
                return "exception";
            }
            finally
            {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            /*String roleAdmin = "[{\"roles\":\"a:0:{ROLE_ADMIN}\"}]";
            Log.e("role", String.valueOf(role));
            Log.e("role2", roleAdmin);

            if(roleAdmin.equals(String.valueOf(role)))
            {
                Toast.makeText(MainActivity.this, "Welcome Admin", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Welcome User", Toast.LENGTH_LONG).show();
            }*/

            if(result.equalsIgnoreCase("true"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */

                Intent intent = new Intent(MainActivity.this,NavigationDrawer.class);
                try {
                    intent.putExtra("visiteurId", String.valueOf(MyVisiteur.getJSONObject(0).getInt("id")));
                    intent.putExtra("visiteurNom", String.valueOf(MyVisiteur.getJSONObject(0).get("username")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
                MainActivity.this.finish();

            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(MainActivity.this, "OoPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }

    }

    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getFragmentManager();

        if(fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    //hides keyboard
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}
