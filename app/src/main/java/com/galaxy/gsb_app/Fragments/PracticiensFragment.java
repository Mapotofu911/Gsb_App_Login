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
import android.widget.TabHost;
import android.widget.TextView;

import com.galaxy.gsb_app.Handler.HttpHandler;
import com.galaxy.gsb_app.Class.Practiciens;
import com.galaxy.gsb_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PracticiensFragment extends Fragment{

    // array list for spinner adapter

    private ArrayList<Practiciens> practicienList;
    private AutoCompleteTextView autoCompleteTextPract;

    // API urls
    // Url to get all Practiciens

    private String url = "http://10.0.2.2/apigsb/getPracticiens.php";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practiciens, container, false);

        practicienList = new ArrayList<>();

        final TextView PractNom = (TextView) view.findViewById(R.id.textViewMedNom);
        final TextView PractPrenom = (TextView)view.findViewById(R.id.textViewPrenom);
        final TextView PractAdresse = (TextView)view.findViewById(R.id.textViewVisiteurAdresse);
        final TextView PractCP = (TextView)view.findViewById(R.id.textViewCP);
        final TextView PractType = (TextView)view.findViewById(R.id.textViewType);
        final TextView PractCoeff = (TextView)view.findViewById(R.id.textViewCoeff);
        final TextView PractTel = (TextView)view.findViewById(R.id.textViewTel);
        final Button buttonPractOk = (Button)view.findViewById(R.id.buttonPractOk);
        TabHost mTabHost = (TabHost)view.findViewById(R.id.TabHostPract);
        mTabHost.setup();

        TabHost.TabSpec mSpec = mTabHost.newTabSpec("First Tab");
        mSpec.setContent(R.id.tab1);
        mSpec.setIndicator("Voir");
        mTabHost.addTab(mSpec);
        //Lets add the second Tab
        mSpec = mTabHost.newTabSpec("Second Tab");
        mSpec.setContent(R.id.tab2);
        mSpec.setIndicator("Ajouter");
        mTabHost.addTab(mSpec);


        autoCompleteTextPract = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextPract);
        autoCompleteTextPract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                autoCompleteTextPract.showDropDown();
            }
        });

        buttonPractOk.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view)
             {
                 String Name = autoCompleteTextPract.getText().toString();

                 System.out.println(Name);

                 Practiciens p = new Practiciens();

                 for (int i = 0; i < practicienList.size(); i++)
                 {
                     if (practicienList.get(i).getNom().toUpperCase().equals(Name.toUpperCase()))
                     {
                         p = practicienList.get(i);
                     }
                 }

                 PractNom.setText(p.getNom());
                 PractPrenom.setText(p.getPrenom());
                 PractAdresse.setText(p.getAdresse());
                 PractCP.setText(p.getCodePostal());
                 PractType.setText(p.getType());
                 PractCoeff.setText(p.getCoeffNotoriete());
                 PractTel.setText(p.getTel());

             }
         }
        );


        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        new GetPracticiens().execute();

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Practiciens");
    }

    private class GetPracticiens extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);

            Log.e("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    if (jsonObj != null) {
                        JSONArray Practiciens = jsonObj.getJSONArray("Practiciens");

                        for (int i = 0; i < Practiciens.length(); i++) {

                            JSONObject PractObj = Practiciens.getJSONObject(i);

                            Practiciens p = new Practiciens();

                            p.setId(PractObj.getInt("id"));
                            p.setNom(PractObj.getString("Nom"));
                            p.setPrenom(PractObj.getString("Prenom"));
                            p.setAdresse(PractObj.getString("Adresse"));
                            p.setCodePostal(PractObj.getString("CodePostal"));
                            p.setCoeffNotoriete(PractObj.getString("CoeffNotoriete"));
                            p.setType(PractObj.getString("Type"));
                            p.setTel(PractObj.getString("Telephone"));

                            practicienList.add(p);
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

        for (int i = 0; i < practicienList.size(); i++) {
            lables.add(practicienList.get(i).getNom());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter;
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        autoCompleteTextPract.setAdapter(spinnerAdapter);
    }

}