package com.galaxy.gsb_app.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.gsb_app.Class.CompteRenduSingleton;
import com.galaxy.gsb_app.Class.RequestHandler;
import com.galaxy.gsb_app.Handler.HttpHandler;
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
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Mapotofu on 30/03/2017.
 */

public class FinaliserCompteRendu extends Fragment {

    CompteRenduSingleton crs;
    private CheckBox checkBoxRemplacement;
    private CheckBox checkBoxDocumentation;
    private CheckBox checkBoxSaisie;
    private EditText editTextMotif;
    private EditText editTextBilan;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_finaliser_compte_rendu, container, false);
        crs = CompteRenduSingleton.getInstance();

        final CheckBox checkBoxRemplacement = (CheckBox)view.findViewById(R.id.checkBoxRemplacement);
        final CheckBox checkBoxDocumentation = (CheckBox)view.findViewById(R.id.checkBoxDocumentation);
        final CheckBox checkBoxSaisie = (CheckBox)view.findViewById(R.id.checkBoxSaisie);
        final EditText editTextMotif = (EditText)view.findViewById(R.id.editTextMotif);
        final EditText editTextBilan = (EditText)view.findViewById(R.id.editTextBilan);

        Bundle args2 = getArguments();

        if (args2.getInt("cptid") != -1)
        {
            checkBoxRemplacement.setChecked(crs.getRemplacant());
            checkBoxDocumentation.setChecked(crs.getDocumentation());
            checkBoxSaisie.setChecked(crs.getSaisieDefinitive());

            editTextMotif.setText(crs.getMotif());
            editTextBilan.setText(crs.getBilan());
        }

        Button buttonSoumettre = (Button)view.findViewById(R.id.buttonSoumettre);
        buttonSoumettre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                crs.setBilan(String.valueOf(editTextBilan.getText()));
                crs.setMotif(String.valueOf(editTextMotif.getText()));

                if (checkBoxDocumentation.isChecked())
                    crs.setDocumentation(true);
                else
                    crs.setDocumentation(false);

                if (checkBoxRemplacement.isChecked())
                    crs.setRemplacant(true);
                else
                    crs.setRemplacant(false);

                if (checkBoxSaisie.isChecked())
                    crs.setSaisieDefinitive(true);
                else
                    crs.setSaisieDefinitive(false);

                updateEmployee();

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

    private void updateEmployee() {

        class UpdateEmployee extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity(), "Message", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {

                Bundle args2 = getArguments();

                HashMap<String, String> hashMap = new HashMap<>();

                if(args2.getInt("cptid")!=-1)
                {
                    hashMap.put("id", String.valueOf(crs.getId()));

                }

                hashMap.put("motif", String.valueOf(crs.getMotif()));
                hashMap.put("practiciens_id", String.valueOf(crs.getPrecticien_id()));
                hashMap.put("visiteurs_id", String.valueOf(crs.getVisiteur_rapport_id()));
                hashMap.put("bilan", String.valueOf(crs.getBilan()));
                hashMap.put("cptid", String.valueOf(args2.getInt("cptid")));

                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();
                String strDate = sdfDate.format(now);

                Log.e("date", String.valueOf(crs.getVisiteur_rapport_id()));

                hashMap.put("dateNew", strDate);

                if (!crs.getSaisieDefinitive()) {
                    hashMap.put("SaisieDefinitive", String.valueOf(0));
                } else {
                    hashMap.put("SaisieDefinitive", String.valueOf(1));
                }

                if (!crs.getDocumentation()) {
                    hashMap.put("documentation", String.valueOf(0));
                } else {
                    hashMap.put("documentation", String.valueOf(1));
                }

                if (!crs.getRemplacant()) {
                    hashMap.put("remplacant", String.valueOf(0));
                } else {
                    hashMap.put("remplacant", String.valueOf(1));
                }


                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest("http://10.0.2.2/apigsb/updateCompteRendu.php", hashMap);

                Log.e("s", s);

                return s;
            }
        }

        UpdateEmployee ue = new UpdateEmployee();
        ue.execute();
    }



}
