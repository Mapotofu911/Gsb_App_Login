package com.galaxy.gsb_app.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.galaxy.gsb_app.Handler.HttpHandler;
import com.galaxy.gsb_app.R;
import com.galaxy.gsb_app.Class.Region;
import com.galaxy.gsb_app.Class.Visiteurs;

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
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class VisiteursFragment extends Fragment {

    private ArrayList<Visiteurs> visiteursList;
    private ArrayList<Region> regionList;
    private Spinner spinnerRegion;
    private Region UneRegion;
    private String NomRegion;
    private int idRegion;
    private AutoCompleteTextView autoCompleteTextViewVisiteur;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_visiteurs, container, false);

        regionList = new ArrayList<>();
        //visiteursList = new ArrayList<>();

        spinnerRegion = (Spinner)view.findViewById(R.id.spinnerRegion);

        autoCompleteTextViewVisiteur = (AutoCompleteTextView)view.findViewById(R.id.autoCompleteTextViewVisiteur);
        autoCompleteTextViewVisiteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {autoCompleteTextViewVisiteur.showDropDown();}
        });

        final TextView textViewVisteurNom = (TextView)view.findViewById(R.id.textViewVisteurNom);
        final TextView textViewVisteurPrenom = (TextView)view.findViewById(R.id.textViewVisteurPrenom);
        final TextView textViewVisteurLaboratoire = (TextView)view.findViewById(R.id.textViewVisteurLaboratoire);
        final TextView textViewVisteurMail = (TextView)view.findViewById(R.id.textViewVisteurMail);
        final TextView textViewVisteurTelephone = (TextView)view.findViewById(R.id.textViewVisteurTelephone);
        final TextView textViewVisiteurSecteur = (TextView)view.findViewById(R.id.textViewVisiteurSecteur);
        final TextView textViewVisteurCodepostal = (TextView)view.findViewById(R.id.textViewVisteurCodepostal);
        final TextView textViewVisiteurAdresse = (TextView)view.findViewById(R.id.textViewVisiteurAdresse);

        final Button buttonOkVisiteurs = (Button)view.findViewById(R.id.buttonOkVisiteurs);


        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id)
            {
                NomRegion = spinnerRegion.getSelectedItem().toString();

                for (int i = 0; i < regionList.size(); i++)
                {
                    if (regionList.get(i).getLibelle().toUpperCase().equals(NomRegion.toUpperCase()))
                    {
                        UneRegion = regionList.get(i);
                    }
                }
                idRegion = UneRegion.getId();
                new VisiteursFragment.SendRequest().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                NomRegion = spinnerRegion.getSelectedItem().toString();

                for (int i = 0; i < regionList.size(); i++)
                {
                    if (regionList.get(i).getLibelle().toUpperCase().equals(NomRegion.toUpperCase()))
                    {
                        UneRegion = regionList.get(i);
                    }
                }
                idRegion = UneRegion.getId();
                new VisiteursFragment.SendRequest().execute();

            }
        });

        buttonOkVisiteurs.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view)
                                             {
                                                 String Name = autoCompleteTextViewVisiteur.getText().toString();

                                                 System.out.println(Name);

                                                 Visiteurs v = new Visiteurs();

                                                 for (int i = 0; i < visiteursList.size(); i++)
                                                 {
                                                     if (visiteursList.get(i).getNom().toUpperCase().equals(Name.toUpperCase()))
                                                     {
                                                         v = visiteursList.get(i);
                                                     }
                                                 }

                                                 textViewVisteurNom.setText(v.getNom());
                                                 textViewVisteurPrenom.setText(v.getPrenom());
                                                 textViewVisiteurAdresse.setText(v.getAdresse());
                                                 textViewVisteurCodepostal.setText(v.getCodePostal());
                                                 textViewVisteurLaboratoire.setText(v.getLaboratoire());
                                                 textViewVisteurMail.setText(v.getMail());
                                                 textViewVisteurTelephone.setText(v.getTel());
                                                 textViewVisiteurSecteur.setText(v.getSecteur());

                                             }
                                         }
        );

        new VisiteursFragment.GetRegion().execute();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Visiteurs");
    }

    private class GetRegion extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall("http://rulliereolivier.fr/apigsb/getRegion.php");

            Log.e("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    if (jsonObj != null) {
                        JSONArray JRegion = jsonObj.getJSONArray("Regions");

                        for (int i = 0; i < JRegion.length(); i++) {

                            JSONObject RegionObj = JRegion.getJSONObject(i);

                            Region r = new Region();

                            r.setId(RegionObj.getInt("id"));
                            r.setLibelle(RegionObj.getString("Libelle"));

                            regionList.add(r);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            populateSpinner();
        }
    }

    private void populateSpinner() {
        List<String> lables = new ArrayList<>();

        for (int i = 0; i < regionList.size(); i++) {
            lables.add(regionList.get(i).getLibelle());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter;
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerRegion.setAdapter(spinnerAdapter);
    }

    private class SendRequest extends AsyncTask<String, Void, String> {

        HttpURLConnection conn;

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try{

                URL url = new URL("http://rulliereolivier.fr/apigsb/getVisiteurs.php");

                JSONObject postDataParams = new JSONObject();

                //set params
                postDataParams.put("region", idRegion);


                Log.e("params",postDataParams.toString());

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

            } catch (Exception e) {
                e.printStackTrace();
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
                    Log.e("1st", String.valueOf(result));
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            visiteursList = new ArrayList<>();

            //Toast.makeText(getActivity().getApplicationContext(), result,Toast.LENGTH_LONG).show();
            Log.e("2nd", result);

            if (result != null) {
                try {
                    JSONObject jsonObj = new JSONObject(result);

                    if (jsonObj != null) {

                        JSONArray Visiteurs = jsonObj.getJSONArray("Visiteurs");

                        for (int i = 0; i < Visiteurs.length(); i++) {

                            JSONObject VisiteurObj = Visiteurs.getJSONObject(i);

                            Visiteurs v = new Visiteurs();

                            v.setId(VisiteurObj.getInt("id"));
                            v.setNom(VisiteurObj.getString("Nom"));
                            v.setPrenom(VisiteurObj.getString("Prenom"));
                            v.setAdresse(VisiteurObj.getString("Adresse"));
                            v.setCodePostal(VisiteurObj.getString("CodePostal"));
                            v.setRegion(VisiteurObj.getString("Libelle"));
                            v.setSecteur(VisiteurObj.getString("Secteur"));
                            v.setTel(VisiteurObj.getString("Tel"));
                            v.setMail(VisiteurObj.getString("email"));
                            v.setLaboratoire(VisiteurObj.getString("Laboratoire"));

                            visiteursList.add(v);

                        }
                        populateAutoCompleteTextView();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }

        Log.e("params2", result.toString());

        return result.toString();
    }

    private void populateAutoCompleteTextView() {

        List<String> lablesVisisteurs = new ArrayList<>();

        for (int i = 0; i < visiteursList.size(); i++) {
            lablesVisisteurs.add(visiteursList.get(i).getNom());

            Log.e("Labels",visiteursList.get(i).getNom());

        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter;
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lablesVisisteurs);

        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        autoCompleteTextViewVisiteur.setAdapter(spinnerAdapter);
    }
}
