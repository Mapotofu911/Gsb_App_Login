package com.galaxy.gsb_app.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.galaxy.gsb_app.Class.News;
import com.galaxy.gsb_app.Class.NewsSingleton;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.sql.Types.NULL;


public class AgendaFragment extends Fragment {
    CalendarView simpleCalendarView;
    private String selectedDate;
    private ListView listViewAgenda;
    private ArrayList<News> newsList;
    NewsSingleton newsSingleton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        final View view = inflater.inflate(R.layout.fragment_agenda, container, false);

        simpleCalendarView = (CalendarView)view.findViewById(R.id.simpleCalendarView);
        listViewAgenda = (ListView)view.findViewById(R.id.listViewAgenda);

        newsSingleton = NewsSingleton.getInstance();


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
        selectedDate = format1.format(cal.getTime());
        Log.e("date", selectedDate);
        new AgendaFragment.getEvenements().execute();


        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                int mois = month + 1;

                Toast.makeText(getContext(), dayOfMonth +"/"+mois+"/"+ year, Toast.LENGTH_LONG).show();

                selectedDate = year+"-"+mois+"-"+dayOfMonth;

                new AgendaFragment.getEvenements().execute();
            }});

        listViewAgenda.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                TextView newsTitle = ((TextView)view.findViewById(android.R.id.text1));
                TextView newsContent = ((TextView)view.findViewById(android.R.id.text2));

                String newsContentS = newsContent.getText().toString();
                String newsTitleS = newsTitle.getText().toString();


                Log.e("newsTitle", newsTitleS);
                Log.e("newsAuthor", newsContentS);

                for (int i = 0; i < newsList.size(); i++) {
                    if (newsList.get(i).getTitle().equals(newsTitleS) && newsList.get(i).getContent().equals(newsContentS))
                    {
                        newsSingleton.setContent(newsList.get(i).getContent());
                        newsSingleton.setDate(newsList.get(i).getDate());
                        newsSingleton.setPlaceNumber(newsList.get(i).getPlaceNumber());
                        newsSingleton.setAuthor(newsList.get(i).getAuthor());
                        newsSingleton.setTitle(newsList.get(i).getTitle());
                        newsSingleton.setId(newsList.get(i).getId());
                    }
                }

                Log.e("newsSingleton", String.valueOf(newsSingleton.getPlaceNumber()));


                ModifierEvenementsFragment myFrag2 = new ModifierEvenementsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, myFrag2).addToBackStack("AgendaFragment").commit();
            }
        });

        Button buttonAjouter = (Button)view.findViewById(R.id.buttonAjouter);
        buttonAjouter.setOnClickListener(new View.OnClickListener()
             {
                 @Override
                 public void onClick(View v) {

                     Date cDate = new Date();
                     String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

                     Bundle args2 = getArguments();

                     newsSingleton.setContent("");
                     newsSingleton.setDate(fDate);
                     newsSingleton.setPlaceNumber(NULL);
                     newsSingleton.setAuthor(args2.getString("username"));
                     newsSingleton.setTitle("");
                     newsSingleton.setId(-1);

                     ModifierEvenementsFragment myFrag = new ModifierEvenementsFragment();
                     getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, myFrag).addToBackStack("ModifierCompteRendusFragment").commit();                 }
             }
        );

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Agenda");
    }

    private void afficherListeNews(){

        List<Map<String, String>> lables = new ArrayList<>();

        for (int i = 0; i < newsList.size(); i++) {
            Map<String, String> MapNews = new HashMap<>();
            MapNews.put("First Line", newsList.get(i).getTitle());
            MapNews.put("Second Line", newsList.get(i).getContent());

            lables.add(MapNews);
        }

        SimpleAdapter adapterNews = new SimpleAdapter(getContext(), lables, android.R.layout.simple_list_item_2, new String[]{"First Line", "Second Line"}, new int[]{android.R.id.text1, android.R.id.text2});
        listViewAgenda.setAdapter(adapterNews);
    }

    private class getEvenements extends AsyncTask<String, Void, String> {
        HttpURLConnection conn;

        protected String doInBackground(String... arg0) {

            try {
                Bundle args2 = getArguments();

                URL url = new URL("http://10.0.2.2/apigsb/getEvenements.php");

                HashMap<String,String> paramsNews = new HashMap<>();
                paramsNews.put("date", String.valueOf(selectedDate));
                paramsNews.put("username", args2.getString("username"));

                JSONObject postDataParams = new JSONObject(paramsNews);

                Log.e("paramsOff", postDataParams.toString());

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
                    return (result.toString());

                } else {

                    return ("unsuccessful");
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

            newsList = new ArrayList<>();

            if (result != null) {
                try {
                    JSONObject jsonObj = new JSONObject(result);

                    if (jsonObj != null) {

                        JSONArray newsJs = jsonObj.getJSONArray("Evenements");

                        for (int i = 0; i < newsJs.length(); i++) {

                            JSONObject newsJsObjct = newsJs.getJSONObject(i);

                            News n = new News();

                            n.setId(newsJsObjct.getInt("id"));
                            n.setTitle(newsJsObjct.getString("title"));
                            n.setAuthor(newsJsObjct.getString("author"));
                            n.setContent(newsJsObjct.getString("content"));
                            n.setDate(newsJsObjct.getString("date"));
                            n.setPlaceNumber(newsJsObjct.getInt("PlaceNumber"));

                            newsList.add(n);
                        }
                        afficherListeNews();
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
                result.append(" & ");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }

        Log.e("params3", result.toString());

        return result.toString();
    }
}