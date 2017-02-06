package com.galaxy.gsb_app;

/**
 * Created by Mapotofu on 24/01/2017.
 */


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MedicamentsFragment extends Fragment {

    private Spinner spinMedicament;
    // array list for spinner adapter
    private ArrayList<Medicaments> medicamentsList;
    // API urls
    // Url to get all Practiciens
    private String url = "http://192.168.43.76/apigsb/getMedicaments.php";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_medicaments, container, false);

        medicamentsList = new ArrayList<>();
        spinMedicament = (Spinner) view.findViewById(R.id.spinnerMedicament);


        Button btn = (Button)view.findViewById(R.id.button2);
        final TextView textViewMedNom = (TextView) view.findViewById(R.id.textViewMedNom);
        final TextView textViewMedDepotLegale = (TextView) view.findViewById(R.id.textViewMedDepotLegale);
        final TextView textViewMedPrix = (TextView) view.findViewById(R.id.textViewMedPrix);
        final TextView textViewMedNomCommerciale = (TextView) view.findViewById(R.id.textViewMedNomCommerciale);
        final TextView textViewMedFamille = (TextView) view.findViewById(R.id.textViewMedFamille);
        final TextView textViewCompo = (TextView) view.findViewById(R.id.textViewCompo);
        final TextView textViewIndication = (TextView) view.findViewById(R.id.textViewIndication);
        final TextView textViewEffets = (TextView) view.findViewById(R.id.textViewEffets);


        //TabHost
        TabHost mTabHost = (TabHost)view.findViewById(R.id.TabHostMed);
        mTabHost.setup();

        TabHost.TabSpec mSpec = mTabHost.newTabSpec("First Tab");
        mSpec.setContent(R.id.Composition);
        mSpec.setIndicator("Composition");
        mTabHost.addTab(mSpec);
        //Lets add the second Tab
        mSpec = mTabHost.newTabSpec("Second Tab");
        mSpec.setContent(R.id.Contre_Indications);
        mSpec.setIndicator("Contre Indications");
        mTabHost.addTab(mSpec);
        //Lets add the third Tab
        mSpec = mTabHost.newTabSpec("Third Tab");
        mSpec.setContent(R.id.Effets);
        mSpec.setIndicator("Effets");
        mTabHost.addTab(mSpec);


        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view)
                                   {
                                       String Name = spinMedicament.getSelectedItem().toString();
                                       Medicaments m = new Medicaments();

                                       for (int i = 0; i < medicamentsList.size(); i++)
                                       {
                                           if (medicamentsList.get(i).getNomMed() == Name)
                                           {
                                               m = medicamentsList.get(i);
                                           }
                                       }

                                       textViewMedNom.setText(m.getNomMed());
                                       textViewMedDepotLegale.setText(m.getDepotLegale());
                                       textViewMedPrix.setText(m.getPrix());
                                       textViewMedNomCommerciale.setText(m.getNomCommerciale());
                                       textViewMedFamille.setText(m.getFamille());
                                       textViewCompo.setText(m.getComposition());
                                       textViewIndication.setText(m.getContreIndications());
                                       textViewEffets.setText(m.getEffets());
                                   }
                               }
        );

        new MedicamentsFragment.GetMedicaments().execute();
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Medicaments");
    }

    private class GetMedicaments extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);

            Log.e("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    if (jsonObj != null) {
                        JSONArray Medicaments = jsonObj.getJSONArray("Medicaments");

                        for (int i = 0; i < Medicaments.length(); i++) {

                            JSONObject MedObj = Medicaments.getJSONObject(i);

                            int id = MedObj.getInt("id");
                            String NomMed = MedObj.getString("NomMed");
                            String DepotLegale = MedObj.getString("DepotLegale");
                            String NomCommerciale = MedObj.getString("NomCommerciale");
                            String Famille = MedObj.getString("Famille");
                            String Prix = MedObj.getString("Prix");
                            String Composition = MedObj.getString("Composition");
                            String Effets = MedObj.getString("Effets");
                            String ContreIndications = MedObj.getString("ContreIndications");

                            Medicaments m = new Medicaments();

                            m.setId(id);
                            m.setComposition(Composition);
                            m.setContreIndications(ContreIndications);
                            m.setDepotLegale(DepotLegale);
                            m.setNomMed(NomMed);
                            m.setNomCommerciale(NomCommerciale);
                            m.setFamille(Famille);
                            m.setPrix(Prix);
                            m.setEffets(Effets);

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

            populateSpinner();
        }

    }

    /**
     * Adding spinner data
     * */
    private void populateSpinner() {
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
        spinMedicament.setAdapter(spinnerAdapter);
    }
}