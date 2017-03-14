package com.galaxy.gsb_app.Fragments;

        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ListView;
        import android.widget.TabHost;

        import com.galaxy.gsb_app.Class.CompteRendus;
        import com.galaxy.gsb_app.Class.CompteRendusAdapter;
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
        import java.util.Iterator;
        import java.util.List;


public class CompteRendusFragment extends Fragment {

    ListView listComptesRendues;
    private List<CompteRendus> compteRendusList;
    String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        compteRendusList = new ArrayList<>();

        //get user id
        id = getArguments().getString("visiteurId");
        Log.e("paramsid", id);

        View view = inflater.inflate(R.layout.fragment_compte_rendus, container, false);

        TabHost TabHostComptesRendues = (TabHost)view.findViewById(R.id.TabHostComptesRendues);
        TabHostComptesRendues.setup();

        TabHost.TabSpec mSpec = TabHostComptesRendues.newTabSpec("First Tab");
        mSpec.setContent(R.id.Voir);
        mSpec.setIndicator("Voir");
        TabHostComptesRendues.addTab(mSpec);
        //Lets add the second Tab
        mSpec = TabHostComptesRendues.newTabSpec("Second Tab");
        mSpec.setContent(R.id.Ajouter);
        mSpec.setIndicator("Ajouter");
        TabHostComptesRendues.addTab(mSpec);
        //Lets add the third Tab
        mSpec = TabHostComptesRendues.newTabSpec("Third Tab");
        mSpec.setContent(R.id.Modifier);
        mSpec.setIndicator("Modifier");
        TabHostComptesRendues.addTab(mSpec);

        listComptesRendues = (ListView)view.findViewById(R.id.listComptesRendues);

        new CompteRendusFragment.GetComptesRendus().execute();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Compte Rendus");
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

                URL url = new URL("http://10.0.2.2/apigsb/getCompteRendus.php");

                JSONObject postDataParams = new JSONObject();


                postDataParams.put("visiteursId", id);


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


            if (result != null) {
                try {
                    JSONObject jsonObj = new JSONObject(result);

                    if (jsonObj != null) {

                        JSONArray cptrendus = jsonObj.getJSONArray("compte_rendus");

                        for (int i = 0; i < cptrendus.length(); i++) {

                            JSONObject cptrendusObj = cptrendus.getJSONObject(i);

                            CompteRendus c = new CompteRendus();


                            c.setId(cptrendusObj.getInt("id"));
                            c.setDate_rapport(cptrendusObj.getString("dateRapport"));
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

                            c.setPracticien_rapport_id(cptrendusObj.getInt("practiciens_id"));
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

}