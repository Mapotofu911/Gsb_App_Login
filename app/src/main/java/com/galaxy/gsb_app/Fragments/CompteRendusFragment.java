package com.galaxy.gsb_app.Fragments;

        import android.app.ProgressDialog;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.galaxy.gsb_app.Class.CompteRendus;
        import com.galaxy.gsb_app.Class.CompteRendusAdapter;
        import com.galaxy.gsb_app.Handler.RequestHandler;
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
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Iterator;
        import java.util.List;

public class CompteRendusFragment extends Fragment {

    private ListView listComptesRendues;
    private List<CompteRendus> compteRendusList;
    private String idVisiteur;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        //compteRendusList = new ArrayList<>();

        //get user id
        idVisiteur = getArguments().getString("visiteurId");
        Log.e("paramsid", idVisiteur);

        final View view = inflater.inflate(R.layout.fragment_compte_rendus, container, false);

        listComptesRendues = (ListView)view.findViewById(R.id.listComptesRendues);

        listComptesRendues.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                //listComptesRendues.getItemAtPosition(position);
                TextView textNumCompteRendue = ((TextView)arg1.findViewById(R.id.NumCompteRendue));

                String selected = textNumCompteRendue.getText().toString();

                Log.e("selected", String.valueOf(selected));

                Integer cptId = null;

                for (int x = 0; x < compteRendusList.size(); x++)
                {
                    if (compteRendusList.get(x).getId() == Integer.valueOf(selected))
                    {
                        cptId = compteRendusList.get(x).getId();
                    }
                }
                ModifierCompteRendusFragment myFrag = new ModifierCompteRendusFragment();
                Bundle args = new Bundle();

                args.putInt("cptid", cptId);
                args.putInt("visiteurId", Integer.valueOf(idVisiteur));
                Log.e("cptid", String.valueOf(cptId));

                myFrag.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, myFrag).addToBackStack("ModifierCompteRendusFragment").commit();
            }
        });

        final Button buttonAjouter = (Button)view.findViewById(R.id.buttonAjouter);
        buttonAjouter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ModifierCompteRendusFragment myFrag = new ModifierCompteRendusFragment();
                Bundle args = new Bundle();
                int cptId = -1;
                args.putInt("cptid", cptId);
                args.putInt("visiteurId", Integer.valueOf(idVisiteur));
                Log.e("cptid", String.valueOf(cptId));

                myFrag.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, myFrag).addToBackStack("ModifierCompteRendusFragment").commit();
            }
        });

        final EditText editTextDelete = (EditText)view.findViewById(R.id.editTextDelete);
        final Button buttonDelete = (Button)view.findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (editTextDelete.getText().toString().equals(""))
                {
                    //loading.dismiss();
                    Toast.makeText(getActivity(), "Veuillez entrez un numéro de rapport.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    deleteCompteRendu(editTextDelete.getText().toString());
                    new CompteRendusFragment.GetComptesRendus().execute();
                }
            }
        });

        new CompteRendusFragment.GetComptesRendus().execute();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Comptes - Rendus");
    }

    private void afficherListeCompteRendus(){

        List<CompteRendus> listRapport = compteRendusList;

        CompteRendusAdapter adapter = new CompteRendusAdapter(getContext(), listRapport);
        listComptesRendues.setAdapter(adapter);
    }


    private class GetComptesRendus extends AsyncTask<String, String, String> {

        HttpURLConnection conn;

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try{

                URL url = new URL("http://rulliereolivier.fr/apigsb/getCompteRendus.php");

                JSONObject postDataParams = new JSONObject();


                postDataParams.put("visiteursId", idVisiteur);


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

            compteRendusList = new ArrayList<>();

            if (result != null) {
                try {
                    JSONObject jsonObj = new JSONObject(result);

                    if (jsonObj != null) {

                        JSONArray cptrendus = jsonObj.getJSONArray("compte_rendus");

                        for (int i = 0; i < cptrendus.length(); i++) {

                            JSONObject cptrendusObj = cptrendus.getJSONObject(i);

                            CompteRendus c = new CompteRendus();


                            c.setId(cptrendusObj.getInt("id"));
                            c.setDate_rapport(cptrendusObj.getString("date"));
                            c.setMotif(cptrendusObj.getString("motif"));
                            c.setBilan(cptrendusObj.getString("bilan"));

                            if (cptrendusObj.getInt("SaisieDefinitive") == 0)
                            {
                                c.setSaisieDefinitive(false);
                            }
                            else
                            {
                                c.setSaisieDefinitive(true);
                            }

                            if (cptrendusObj.getInt("documentation") == 0)
                            {
                                c.setDocumentation(false);
                            }
                            else
                            {
                                c.setDocumentation(true);
                            }

                            if (cptrendusObj.getInt("remplacant") == 0)
                            {
                                c.setRemplacant(false);
                            }
                            else
                            {
                                c.setRemplacant(true);
                            }

                            c.setPracticien_nom(cptrendusObj.getString("NomPract"));
                            c.setPract_id(cptrendusObj.getInt("practiciens_id"));
                            c.setVisiteur_rapport_id(cptrendusObj.getInt("visiteurs_id"));

                            compteRendusList.add(c);

                        }

                        afficherListeCompteRendus();
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

    private void deleteCompteRendu(String myEditTextDelete) {

        class deleteCompteRendu extends AsyncTask<String, String, String> {

            ProgressDialog loading;
            String rep2;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity(), rep2, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                boolean cptExist = false;
                String editTextDelete = params[0];

                for (int i = 0; i < compteRendusList.size(); i++) {

                    if (compteRendusList.get(i).getId() == Integer.valueOf(String.valueOf(editTextDelete)))
                    {
                        cptExist = true;
                    }
                }

                if(cptExist)
                {
                    RequestHandler rh = new RequestHandler();
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("cptId", String.valueOf(editTextDelete));
                    hm.put("visiteurId", String.valueOf(idVisiteur));

                    rep2 = rh.sendPostRequest("http://rulliereolivier.fr/apigsb/deleteCompteRendu.php", hm);
                    Log.e("Delete rep", rep2);
                    return rep2;
                }
                else
                {
                    return rep2 = "Ce numéro de compte rendu n'existe pas.";
                }
            }
        }

        deleteCompteRendu ue = new deleteCompteRendu();
        ue.execute(myEditTextDelete);
    }
}