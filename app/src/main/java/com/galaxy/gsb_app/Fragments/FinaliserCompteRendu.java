package com.galaxy.gsb_app.Fragments;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.galaxy.gsb_app.Class.CompteRenduSingleton;
import com.galaxy.gsb_app.Handler.RequestHandler;
import com.galaxy.gsb_app.R;

import java.util.HashMap;

import static java.sql.Types.NULL;

/**
 * Created by Mapotofu on 30/03/2017.
 */

public class FinaliserCompteRendu extends Fragment {

    CompteRenduSingleton crs;

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

                if (editTextBilan.getText().toString().equals(""))
                {
                    crs.setBilan(" ");
                }
                else
                {
                    crs.setBilan(String.valueOf(editTextBilan.getText()));
                }

                if (editTextMotif.getText().toString().equals(""))
                {
                    crs.setBilan(" ");
                }
                else
                {
                    crs.setMotif(String.valueOf(editTextMotif.getText()));
                }

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

                updateCompteRendu();


                CompteRendusFragment myFrag = new CompteRendusFragment();
                Bundle args = new Bundle();
                args.putString("visiteurId", String.valueOf(crs.getVisiteur_rapport_id()));
                myFrag.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, myFrag).addToBackStack(null).commit();
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

    private void updateCompteRendu() {

        class updateCompteRendu extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getActivity(), "Compte rendu mis à jour", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {

                Bundle args2 = getArguments();

                HashMap<String, String> hashMap = new HashMap<>();

                if(args2.getInt("cptid")!= -1)
                {
                    hashMap.put("id", String.valueOf(crs.getId()));
                }

                hashMap.put("motif", String.valueOf(crs.getMotif()));
                hashMap.put("practiciens_id", String.valueOf(crs.getPrecticien_id()));
                hashMap.put("visiteurs_id", String.valueOf(crs.getVisiteur_rapport_id()));
                hashMap.put("bilan", String.valueOf(crs.getBilan()));
                hashMap.put("cptid", String.valueOf(args2.getInt("cptid")));

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

                String repId = rh.sendPostRequest("http://rulliereolivier.fr/apigsb/updateCompteRendu.php", hashMap);
                Log.e("repId", repId);

                String rep ="";

                if (crs.getId() != NULL && crs.getId()!= -1)
                {
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("cptId", String.valueOf(crs.getId()));
                    hm.put("visiteurId", String.valueOf(crs.getVisiteur_rapport_id()));
                    rep = rh.sendPostRequest("http://rulliereolivier.fr/apigsb/deleteCompteRenduMed.php", hm);
                    Log.e("s1", rep);
                }

                if(crs.getId() == -1)
                {
                    crs.setId(Integer.valueOf(repId));
                }

                for (int i = 0; i < crs.getMedicamentsOfferts_rapport().size(); i++)
                {
                    HashMap<String,String> hm = new HashMap<>();
                    hm.put("medId",String.valueOf(crs.getMedicamentsOfferts_rapport().get(i).getMedicament().getId()));
                    hm.put("quantity",String.valueOf(crs.getMedicamentsOfferts_rapport().get(i).getQuantity()));
                    hm.put("cptId", String.valueOf(crs.getId()));

                    rep = rh.sendPostRequest("http://rulliereolivier.fr/apigsb/updateCompteRenduMedicamentsOfferts.php", hm);
                    Log.e("s2", rep);
                }

                for(int i = 0; i < crs.getMedicamentsPresente_rapport().size(); i++)
                {
                    HashMap<String,String> hm = new HashMap<>();
                    hm.put("medId",String.valueOf(crs.getMedicamentsPresente_rapport().get(i).getId()));
                    hm.put("cptId", String.valueOf(crs.getId()));

                    rep = rh.sendPostRequest("http://rulliereolivier.fr/apigsb/updateCompteRenduMedicamentsPresente.php", hm);
                    Log.e("s3", rep);
                }


                return rep;
            }
        }

        updateCompteRendu ue = new updateCompteRendu();
        ue.execute();
    }
}
