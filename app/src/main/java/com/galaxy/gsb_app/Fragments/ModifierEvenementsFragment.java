package com.galaxy.gsb_app.Fragments;


import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.galaxy.gsb_app.Activity.MainActivity;
import com.galaxy.gsb_app.Class.DatePickerFragment;
import com.galaxy.gsb_app.Handler.RequestHandler;

import com.galaxy.gsb_app.Class.NewsSingleton;
import com.galaxy.gsb_app.R;

import java.util.Calendar;
import java.util.HashMap;

import static java.sql.Types.NULL;

/**
 * Created by Mapotofu on 24/04/2017.
 */

public class ModifierEvenementsFragment extends Fragment {

    NewsSingleton newsSingleton;
    EditText editTextDate;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_modifier_evenements, container, false);

        newsSingleton = NewsSingleton.getInstance();

        editTextDate = (EditText)view.findViewById(R.id.editTextDate);
        final EditText editTextTitre = (EditText)view.findViewById(R.id.editTextTitre);
        final EditText editTextContent = (EditText)view.findViewById(R.id.editTextContent);
        final EditText editTextNbPlaces = (EditText)view.findViewById(R.id.editTextNbPlaces);
        Button buttonSave = (Button)view.findViewById(R.id.buttonSave);
        Button buttonDelete = (Button)view.findViewById(R.id.buttonDelete);


        editTextDate.setText(newsSingleton.getDate());
        editTextDate.setInputType(NULL);
        editTextTitre.setText(newsSingleton.getTitle());
        editTextContent.setText(newsSingleton.getContent());

        Log.e("newsSingleton", String.valueOf(newsSingleton.getPlaceNumber()));
        String x = String.valueOf(newsSingleton.getPlaceNumber());

        editTextNbPlaces.setText(x);

        if(newsSingleton.getId() == NULL)
        {
            buttonDelete.setEnabled(false);
        }

        editTextDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                showDatePicker();

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                int x = Integer.parseInt(editTextNbPlaces.getText().toString());

                newsSingleton.setPlaceNumber(x);
                newsSingleton.setTitle(editTextTitre.getText().toString());
                newsSingleton.setContent(editTextContent.getText().toString());
                newsSingleton.setPlaceNumber(Integer.valueOf(editTextNbPlaces.getText().toString()));
                newsSingleton.setDate(editTextDate.getText().toString());
                updateEvenement();

                AgendaFragment myFrag = new AgendaFragment();
                Bundle args = new Bundle();
                args.putString("visiteurId", String.valueOf(newsSingleton.getUser_id()));
                Log.e("visiteurId", String.valueOf(newsSingleton.getUser_id()));
                myFrag.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, myFrag).addToBackStack(null).commit();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                deleteEvenement();

                AgendaFragment myFrag = new AgendaFragment();
                Bundle args = new Bundle();
                args.putString("visiteurId", String.valueOf(newsSingleton.getUser_id()));
                myFrag.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, myFrag).addToBackStack(null).commit();
            }
        });

        return view;
    }

    private void updateEvenement() {

        class updateEvenement extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getActivity(), "Evenement mis à jour", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("date", newsSingleton.getDate());
                hashMap.put("content", newsSingleton.getContent());
                hashMap.put("user_id", String.valueOf(newsSingleton.getUser_id()));
                hashMap.put("PlaceNumber", String.valueOf(newsSingleton.getPlaceNumber()));
                hashMap.put("titre", newsSingleton.getTitle());
                hashMap.put("id", String.valueOf(newsSingleton.getId()));

                Log.e("id", String.valueOf(newsSingleton.getId()));

                RequestHandler rh = new RequestHandler();

                String rep = rh.sendPostRequest("http://rulliereolivier.fr/apigsb/updateEvenement.php", hashMap);
                Log.e("repId", rep);

                return rep;
            }
        }

        updateEvenement ue = new updateEvenement();
        ue.execute();
    }

    private void deleteEvenement() {

        class deleteEvenement extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getActivity(), "Evenement supprimer", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("id", String.valueOf(newsSingleton.getId()));

                Log.e("id", String.valueOf(newsSingleton.getId()));

                RequestHandler rh = new RequestHandler();

                String rep = rh.sendPostRequest("http://rulliereolivier.fr/apigsb/deleteEvenements.php", hashMap);
                Log.e("repId", rep);

                return rep;
            }
        }

        deleteEvenement ue = new deleteEvenement();
        ue.execute();
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            editTextDate.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(dayOfMonth));
        }
    };
}
