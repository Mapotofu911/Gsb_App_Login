package com.galaxy.gsb_app.Fragments;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.galaxy.gsb_app.Class.CompteRenduSingleton;
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

        CheckBox checkBoxRemplacement = (CheckBox)view.findViewById(R.id.checkBoxRemplacement);
        CheckBox checkBoxDocumentation = (CheckBox)view.findViewById(R.id.checkBoxDocumentation);
        CheckBox checkBoxSaisie = (CheckBox)view.findViewById(R.id.checkBoxSaisie);
        EditText editTextMotif = (EditText)view.findViewById(R.id.editTextMotif);
        EditText editTextBilan = (EditText)view.findViewById(R.id.editTextBilan);

        Bundle args2 = getArguments();

        if (args2.getInt("cptid") != -1)
        {
            checkBoxRemplacement.setChecked(crs.getRemplacant());
            checkBoxDocumentation.setChecked(crs.getDocumentation());
            checkBoxSaisie.setChecked(crs.getSaisieDefinitive());

            editTextMotif.setText(crs.getMotif());
            editTextBilan.setText(crs.getBilan());
        }

    return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Compte - Rendus");
    }
}
