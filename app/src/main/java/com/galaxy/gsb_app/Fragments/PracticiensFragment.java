package com.galaxy.gsb_app.Fragments;


import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.galaxy.gsb_app.Handler.HttpHandler;
import com.galaxy.gsb_app.Class.Practiciens;
import com.galaxy.gsb_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.galaxy.gsb_app.R.id.textViewTel;


public class PracticiensFragment extends Fragment{

    // array list for spinner adapter

    private ArrayList<Practiciens> practicienList;
    private AutoCompleteTextView autoCompleteTextPract;

    // API urls
    // Url to get all Practiciens

    private String url = "http://rulliereolivier.fr/apigsb/getPracticiens.php";

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
        final TextView PractTel = (TextView)view.findViewById(textViewTel);
        final Button buttonPractOk = (Button)view.findViewById(R.id.buttonPractOk);


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

        final Button buttonAppel = (Button)view.findViewById(R.id.buttonAppel);
        buttonAppel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {
                if(PractTel.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity().getBaseContext(), "Veuillez selectionner un praticien.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + PractTel.getText().toString()));
                    startActivity(callIntent);
                }
            }

        });


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