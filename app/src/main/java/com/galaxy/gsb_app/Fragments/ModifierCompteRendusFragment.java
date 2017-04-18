package com.galaxy.gsb_app.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.galaxy.gsb_app.Class.CompteRenduMedOfferts;
import com.galaxy.gsb_app.Class.Medicaments;
import com.galaxy.gsb_app.Class.Practiciens;
import com.galaxy.gsb_app.Handler.HttpHandler;
import com.galaxy.gsb_app.R;
import com.galaxy.gsb_app.Class.CompteRenduSingleton;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Created by Mapotofu on 15/03/2017.
 */

public class ModifierCompteRendusFragment extends Fragment{

    private ArrayList<Practiciens> practicienListM;
    private AutoCompleteTextView autoCompleteTextView;
    //private Spinner spinMedicament;
    private AutoCompleteTextView autoCompleteTextViewMed;
    private AutoCompleteTextView autoCompleteTextViewMed2;
    // array list for spinner adapter
    private ArrayList<Medicaments> medicamentsList;
    private ArrayList<String> listMedPresente;
    private List<Map<String, String>> listMedOffert;
    private ArrayAdapter<String> adapterListViewPresente;
    private SimpleAdapter adapterMedOffert;
    CompteRenduSingleton crs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_compte_rendus_modifier, container, false);

        practicienListM = new ArrayList<>();
        medicamentsList = new ArrayList<>();
        listMedPresente = new ArrayList<>();
        listMedOffert = new ArrayList<>();
        crs = CompteRenduSingleton.getInstance();

        final EditText EditTextNb = (EditText)view.findViewById(R.id.EditTextNb);

        adapterListViewPresente = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, listMedPresente);
        adapterMedOffert = new SimpleAdapter(getContext(), listMedOffert, android.R.layout.simple_list_item_2, new String[] {"First Line", "Second Line" }, new int[] {android.R.id.text1, android.R.id.text2 });

        ListView listViewPres = (ListView) view.findViewById(R.id.listViewPres);
        ListView listViewOffert = (ListView) view.findViewById(R.id.listViewOffert);
        listViewPres.setAdapter(adapterListViewPresente);
        listViewOffert.setAdapter(adapterMedOffert);

        new ModifierCompteRendusFragment.GetPracticiens().execute();
        new ModifierCompteRendusFragment.GetMedicaments().execute();

        Bundle args = getArguments();
        Integer x = args.getInt("cptid");
        Log.e("cptid2", x.toString());

        //nouveau ou pas
        if (x == -1) {
            crs.setVisiteur_rapport_id(getArguments().getInt("visiteurId"));
        }

        new ModifierCompteRendusFragment.GetCompteRenduInfo().execute();
        new ModifierCompteRendusFragment.GetCompteRenduMedPres().execute();
        new ModifierCompteRendusFragment.GetCompteRenduMedOff().execute();

        autoCompleteTextViewMed = (AutoCompleteTextView)view.findViewById(R.id.autoCompleteTextViewMed);
        autoCompleteTextViewMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                autoCompleteTextViewMed.showDropDown();
            }
        });

        autoCompleteTextViewMed2 = (AutoCompleteTextView)view.findViewById(R.id.autoCompleteTextViewMed2);
        autoCompleteTextViewMed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                autoCompleteTextViewMed2.showDropDown();
            }
        });

        autoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                autoCompleteTextView.showDropDown();
            }
        });

        final Button buttonPres = (Button)view.findViewById(R.id.buttonPres);
        buttonPres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> medNom = new ArrayList<>();

                for (int i = 0; i < medicamentsList.size(); i++) {
                    medNom.add(medicamentsList.get(i).getNomMed());
                }

                if (medNom.contains(autoCompleteTextViewMed.getText().toString()))
                {
                    Log.e("Ajouter 1 : ","" + autoCompleteTextViewMed.getText());
                    listMedPresente.add(autoCompleteTextViewMed.getText().toString());
                    adapterListViewPresente.notifyDataSetChanged();
                }
                else
                {
                    autoCompleteTextViewMed.setError("Ce médicament n'existe pas");
                }
            }

        });

        final Button buttonOffer = (Button)view.findViewById(R.id.buttonOffer);
        buttonOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Ajouter 2 : ","" + autoCompleteTextViewMed2.getText());
                Log.e("Ajouter 3 : ","" + EditTextNb.getText());

                Map<String, String> MapMed = new HashMap<>();

                ArrayList<String> medNom = new ArrayList<>();

                for (int i = 0; i < medicamentsList.size(); i++) {
                    medNom.add(medicamentsList.get(i).getNomMed());
                }

                if (medNom.contains(autoCompleteTextViewMed2.getText().toString()))
                {
                    if(EditTextNb.getText().toString().equals(""))
                    {
                        EditTextNb.setError("Veuillez entre un nombre d'échanittlons offerts");
                    }
                    else
                    {
                        MapMed.put("First Line", autoCompleteTextViewMed2.getText().toString());
                        MapMed.put("Second Line", EditTextNb.getText().toString());
                        listMedOffert.add(MapMed);
                        adapterMedOffert.notifyDataSetChanged();
                    }
                }
                else
                {
                    autoCompleteTextViewMed2.setError("Ce médicament n'existe pas");
                }
            }
        });

        final Button buttonReset = (Button)view.findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listMedPresente.clear();
                listMedOffert.clear();

                adapterMedOffert.notifyDataSetChanged();
                adapterListViewPresente.notifyDataSetChanged();
            }

        });

        final Button buttonNext = (Button)view.findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> practNom = new ArrayList<>();

                for (int i = 0; i < practicienListM.size(); i++) {

                    practNom.add(practicienListM.get(i).getNom());
                }

                if(practNom.contains(autoCompleteTextView.getText().toString()))
                {
                    ArrayList<Medicaments> PresitMedPres = new ArrayList<>();
                    ArrayList<CompteRenduMedOfferts> PresitMedOff = new ArrayList<>();

                    Integer id = getArguments().getInt("cptid");
                    crs.setId(id);

                    for (int i = 0; i < listMedPresente.size(); i++)
                    {
                        for (int j = 0; j < medicamentsList.size(); j++)
                        {
                            if(Objects.equals(listMedPresente.get(i), medicamentsList.get(j).getNomMed()))
                            {
                                PresitMedPres.add(medicamentsList.get(j));
                                Log.e("listMedPresente", listMedPresente.get(i));
                            }
                        }
                    }
                    crs.setMedicamentsPresente_rapport(PresitMedPres);

                    for (int i = 0; i < listMedOffert.size(); i++)
                    {
                        for (int j = 0; j < medicamentsList.size(); j++)
                        {
                            if(Objects.equals(listMedOffert.get(i).get("First Line"), medicamentsList.get(j).getNomMed()))
                            {
                                CompteRenduMedOfferts m = new CompteRenduMedOfferts();
                                m.setMedicament(medicamentsList.get(j));
                                m.setQuantity(Integer.valueOf(listMedOffert.get(i).get("Second Line")));
                                PresitMedOff.add(m);
                                Log.e("listMedOffert", listMedOffert.get(i).get("First Line"));
                            }

                        }
                    }
                    crs.setMedicamentsOfferts_rapport(PresitMedOff);

                    String PracticienNom = autoCompleteTextView.getText().toString();

                    for (int i = 0; i<practicienListM.size(); i++ )
                    {
                        if (Objects.equals(practicienListM.get(i).getNom(), PracticienNom))
                        {
                            crs.setPrecticien_id(practicienListM.get(i).getId());
                        }
                    }

                    FinaliserCompteRendu nextFrag = new FinaliserCompteRendu();
                    Bundle args2 = new Bundle();
                    Bundle args = getArguments();
                    args2.putInt("cptid", args.getInt("cptid"));
                    args2.putInt("visiteurId", args.getInt("visiteurId"));
                    Log.e("Args2", String.valueOf(args.getInt("cptid")));
                    nextFrag.setArguments(args2);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, nextFrag).addToBackStack("FinaliserCompteRendu").commit();
                }
                else
                {
                    autoCompleteTextView.setError("Ce praticien n'existe pas.");
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Compte - Rendus");
    }


    private class GetPracticiens extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall("http://10.0.2.2/apigsb/getPracticiens.php");

            Log.e("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    if (jsonObj != null) {
                        JSONArray Practiciens = jsonObj.getJSONArray("Practiciens");

                        for (int i = 0; i < Practiciens.length(); i++) {

                            JSONObject PractObj = Practiciens.getJSONObject(i);

                            com.galaxy.gsb_app.Class.Practiciens p = new Practiciens();

                            p.setId(PractObj.getInt("id"));
                            p.setNom(PractObj.getString("Nom"));
                            p.setPrenom(PractObj.getString("Prenom"));
                            p.setAdresse(PractObj.getString("Adresse"));
                            p.setCodePostal(PractObj.getString("CodePostal"));
                            p.setCoeffNotoriete(PractObj.getString("CoeffNotoriete"));
                            p.setType(PractObj.getString("Type"));
                            p.setTel(PractObj.getString("Telephone"));

                            practicienListM.add(p);
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

            populate();
        }

    }

    /**
     * Adding spinner data
     * */
    private void populate() {
        List<String> lables = new ArrayList<>();

        for (int i = 0; i < practicienListM.size(); i++) {
            lables.add(practicienListM.get(i).getNom());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter;
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lables);


        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        autoCompleteTextView.setAdapter(spinnerAdapter);
    }

    private class GetMedicaments extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall("http://10.0.2.2/apigsb/getMedicaments.php");

            Log.e("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    if (jsonObj != null) {

                        JSONArray Medicaments = jsonObj.getJSONArray("Medicaments");

                        for (int i = 0; i < Medicaments.length(); i++) {

                            JSONObject MedObj = Medicaments.getJSONObject(i);

                            Medicaments m = new Medicaments();

                            m.setId(MedObj.getInt("id"));
                            m.setComposition(MedObj.getString("Composition"));
                            m.setContreIndications(MedObj.getString("ContreIndications"));
                            m.setDepotLegale(MedObj.getString("DepotLegale"));
                            m.setNomMed(MedObj.getString("NomMed"));
                            m.setNomCommerciale(MedObj.getString("NomCommerciale"));
                            m.setFamille(MedObj.getString("Famille"));
                            m.setPrix(MedObj.getString("Prix"));
                            m.setEffets(MedObj.getString("Effets"));

                            medicamentsList.add(m);
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

            populateSpinnerMed();
        }
    }

    /**
     * Adding spinner data
     * */
    private void populateSpinnerMed() {
        List<String> lables = new ArrayList<>();

        for (int i = 0; i < medicamentsList.size(); i++) {
            lables.add(medicamentsList.get(i).getNomMed());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter;
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        autoCompleteTextViewMed.setAdapter(spinnerAdapter);
        autoCompleteTextViewMed2.setAdapter(spinnerAdapter);
    }

    private class GetCompteRenduInfo extends AsyncTask<String, Void, String> {

        HttpURLConnection conn;

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://10.0.2.2/apigsb/getCompteRendusById.php");

                JSONObject postDataParams = new JSONObject();

                Integer id = getArguments().getInt("cptid");
                postDataParams.put("compteRendusId", id);

                Log.e("params", postDataParams.toString());

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
                    return (result.toString());

                } else {

                    return ("unsuccessful");
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

            if (result != null) {
                try {

                    JSONObject jsonObj = new JSONObject(result);

                    if (jsonObj != null) {

                            JSONArray cptrendus = jsonObj.getJSONArray("compte_rendus");

                            JSONObject cptrendusObj = cptrendus.getJSONObject(0);

                            crs.setId(cptrendusObj.getInt("id"));
                            crs.setDate_rapport(cptrendusObj.getString("dateRapport"));
                            crs.setMotif(cptrendusObj.getString("motif"));
                            crs.setBilan(cptrendusObj.getString("bilan"));
                            crs.setPracticien_nom(cptrendusObj.getString("Nom"));
                            crs.setVisiteur_rapport_id(cptrendusObj.getInt("visiteurs_id"));

                            Log.e("xD", String.valueOf(crs.getPracticien_nom()));
                            autoCompleteTextView.setText(crs.getPracticien_nom());

                            if (cptrendusObj.getInt("SaisieDefinitive") == 0) {
                                CompteRenduSingleton.getInstance().setSaisieDefinitive(false);
                            } else {
                                CompteRenduSingleton.getInstance().setSaisieDefinitive(true);
                            }

                            if (cptrendusObj.getInt("documentation") == 0) {
                                CompteRenduSingleton.getInstance().setDocumentation(false);
                            } else {
                                CompteRenduSingleton.getInstance().setDocumentation(true);
                            }

                            if (cptrendusObj.getInt("remplacant") == 0) {
                                CompteRenduSingleton.getInstance().setRemplacant(false);
                            } else {
                                CompteRenduSingleton.getInstance().setRemplacant(true);
                            }

                            adapterMedOffert.notifyDataSetChanged();
                            adapterListViewPresente.notifyDataSetChanged();
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

    private class GetCompteRenduMedPres extends AsyncTask<String, Void, String> {
        HttpURLConnection conn;

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://10.0.2.2/apigsb/getMedPresCompteRendu.php");

                JSONObject postDataParams = new JSONObject();

                Integer id = getArguments().getInt("cptid");
                postDataParams.put("compteRendusId", id);

                Log.e("params", postDataParams.toString());

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
                    return (result.toString());

                } else {

                    return ("unsuccessful");
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

            if (result != null) {
                try {

                    JSONObject jsonObj = new JSONObject(result);

                    if (jsonObj != null) {

                        JSONArray med = jsonObj.getJSONArray("med");

                        for (int i = 0; i < med.length(); i++)
                        {
                            JSONObject m = med.getJSONObject(i);
                            listMedPresente.add(m.getString("NomMed"));
                        }

                        adapterMedOffert.notifyDataSetChanged();
                        adapterListViewPresente.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
        }
    }

    private class GetCompteRenduMedOff extends AsyncTask<String, Void, String> {
        HttpURLConnection conn;

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://10.0.2.2/apigsb/getMedOffCompteRendu.php");

                JSONObject postDataParams = new JSONObject();

                Integer id = getArguments().getInt("cptid");
                postDataParams.put("compteRendusId", id);

                Log.e("paramsOff", postDataParams.toString());

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
                    return (result.toString());

                } else {

                    return ("unsuccessful");
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

            if (result != null) {
                try {

                    JSONObject jsonObj = new JSONObject(result);

                    if (jsonObj != null) {

                        JSONArray med = jsonObj.getJSONArray("medOff");

                        for (int i = 0; i < med.length(); i++)
                        {
                            JSONObject m = med.getJSONObject(i);

                            Map<String, String> MapMed = new HashMap<>();

                            MapMed.put("First Line", m.getString("NomMed"));
                            MapMed.put("Second Line", String.valueOf(m.getInt("Quantity")));

                            listMedOffert.add(MapMed);
                        }

                        adapterMedOffert.notifyDataSetChanged();
                        adapterListViewPresente.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
        }
    }
}
