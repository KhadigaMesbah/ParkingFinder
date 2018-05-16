package com.example.android.a3ozaarken;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //private CustomView mcustomView;
    public  ArrayList<String[]> park_data = new ArrayList<String[]>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    private ProgressDialog pDialog;
    String JSON_STRING ;
    JSONArray jsonArray;
    JSONObject jsonObject;




  //  public ArrayList<String[]> getArraylist() {
//        return(park_data);
//    }
    // public Hashtable<Integer, Integer> hash_data = new Hashtable<Integer, Integer>();
    // Hashtable <Integer, String> hashPlaces = new Hashtable<Integer,String>();


    public void getJSON(View view)
    {

        new BackgroundTask().execute();

    }


    class BackgroundTask extends AsyncTask<Void,Void,String>

    {    String json_url ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json_url="http://192.168.1.7:85/index.php?ID=0";
//
//            pDialog = new ProgressDialog(MainActivity.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                System.out.println("hellloagain");
                while((JSON_STRING = bufferedReader.readLine())!=null){

                    stringBuilder.append(JSON_STRING+"\n");
                    System.out.println("helllo");


                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(aVoid);
            TextView textView = (TextView) findViewById(R.id.textView);
            int count = 0 ;
            String places,status,id;

//            if (pDialog.isShowing())
//                pDialog.dismiss();

            try {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray("ParkInfo");



                while(count < jsonArray.length()){

                    JSONObject jo = jsonArray.getJSONObject(count);
                    places = jo.getString("Space");
                    status = jo.getString("State");
                    id = jo.getString("ID");
                    // ParkInfo parkInfo = new ParkInfo(places,status,id);

                    park_data.add(new String[] {id,status});

                    count ++;

                }
                
               CustomView customview ;
                customview = (CustomView) findViewById(R.id.customview);
                textView.setText(park_data.get(1)[1]);
                customview.setValue(park_data);




            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

    }


}
