package com.galaxy.gsb_app;


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
        import android.widget.Spinner;
        import android.widget.TextView;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;


public class PracticiensFragment extends Fragment{

    private Spinner spinPracticien;
    // array list for spinner adapter
    private ArrayList<Practiciens> practicienList;

    // API urls
    // Url to get all Practiciens
    private String url = "http://192.168.43.76/apigsb/getPracticiens.php";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practiciens, container, false);

        practicienList = new ArrayList<>();
        spinPracticien = (Spinner) view.findViewById(R.id.spinPracticien);

        Button btn = (Button)view.findViewById(R.id.btnPract);
        final TextView PractNom = (TextView) view.findViewById(R.id.textViewMedNom);
        final TextView PractPrenom = (TextView)view.findViewById(R.id.textViewPrenom);
        final TextView PractAdresse = (TextView)view.findViewById(R.id.textViewAdresse);
        final TextView PractCP = (TextView)view.findViewById(R.id.textViewCP);
        final TextView PractType = (TextView)view.findViewById(R.id.textViewType);
        final TextView PractCoeff = (TextView)view.findViewById(R.id.textViewCoeff);
        final TextView PractTel = (TextView)view.findViewById(R.id.textViewTel);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String Name = spinPracticien.getSelectedItem().toString();
                Practiciens p = new Practiciens();

                for (int i = 0; i < practicienList.size(); i++)
                {
                    if (practicienList.get(i).getNom() == Name)
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

                            int id = PractObj.getInt("id");
                            String Nom = PractObj.getString("Nom");
                            String Prenom = PractObj.getString("Prenom");
                            String Adresse = PractObj.getString("Adresse");
                            String CodePostal = PractObj.getString("CodePostal");
                            String CoeffNotoriete = PractObj.getString("CoeffNotoriete");
                            String Type = PractObj.getString("Type");
                            String Tel = PractObj.getString("Telephone");

                            Practiciens p = new Practiciens();

                            p.setId(id);
                            p.setNom(Nom);
                            p.setPrenom(Prenom);
                            p.setAdresse(Adresse);
                            p.setCodePostal(CodePostal);
                            p.setCoeffNotoriete(CoeffNotoriete);
                            p.setType(Type);
                            p.setTel(Tel);

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

            populateSpinner();
        }

    }

    /**
     * Adding spinner data
     * */
    private void populateSpinner() {
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
        spinPracticien.setAdapter(spinnerAdapter);
    }

}