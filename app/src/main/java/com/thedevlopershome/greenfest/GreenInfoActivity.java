package com.thedevlopershome.greenfest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.thefinestartist.Base;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class GreenInfoActivity extends AppCompatActivity {


    VerticalViewPager verticalViewPager;
    ArrayList<UserDataHolder> dataList;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pukkar_basics);
        Base.initialize(this);

        Bundle flag = getIntent().getExtras();
        if (flag == null) {
            return;
        }

        int keyFlag=flag.getInt("flag",0);
         dataList=new ArrayList<>();
        ArrayList<UserDataHolder> userData=new ArrayList<>();
      if(keyFlag==1){
          setContentView(R.layout.activity_green_info);
          verticalViewPager = (VerticalViewPager) findViewById(R.id.verticleViewPager);
          userData.add(new UserDataHolder("about Pukkar","Green Committee, NITRR is going to organize first ever National Level Green-Fest of India, \"Pukaar-Echoes of Earth\". Being conducted on 17th March, Pukaar will consist of 20+ events and a loads of fun for you all."," ","find us on","https://www.facebook.com/PukaarNITRaipur"));
          verticalViewPager.setAdapter(new VerticlePagerAdapter(this,userData,2));
      }
   else
       if(keyFlag==2) {
           OftenFunctions oftenFunctions=new OftenFunctions( this);
           if(oftenFunctions.isNetworkAvailable()){
          GreenInfoTask greenInfoTask=new GreenInfoTask();
          greenInfoTask.execute();}
          else{
               Snackbar.make(findViewById(R.id.rel),"NO INTERNET CONNECTION",Snackbar.LENGTH_LONG).show();
           }
       }



    }


    private class GreenInfoTask extends AsyncTask<Void,Void,Integer> {


        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        private StringBuilder gaveStringData(){  //to read json data from url
            StringBuilder content = new StringBuilder();
            try {
                String getUrl="http://devlopershome.000webhostapp.com/festUserData.php";
                URL url = new URL(getUrl);
                HttpURLConnection urlConnection =
                        (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // gets the server json data
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream()));
                String next;
                while ((next = bufferedReader.readLine()) != null)
                {
                    content.append(next);
                }
                bufferedReader.close();
                urlConnection.disconnect();
            }
            catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return content;
        }


        @Override
        protected Integer doInBackground(Void... voids) {



            String data=gaveStringData().toString();
            try {
                JSONObject reader = new JSONObject(data);
                JSONArray jArray = reader.getJSONArray("posts");
                for(int i=0;i<jArray.length();i++){
                    JSONObject nestObject=jArray.getJSONObject(i);
                    JSONObject mainObject=nestObject.getJSONObject("post");
                    UserDataHolder userDataHolder=new UserDataHolder(mainObject.getString("name"),mainObject.getString("description"),mainObject.getString("imageUrl"),"find me on",mainObject.getString("fbUrl"));
                    dataList.add(userDataHolder);

                }




            }
            catch(JSONException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }



            return 1;
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            setContentView(R.layout.activity_green_info);
            Toast.makeText(GreenInfoActivity.this, "Swipe up to view next", Toast.LENGTH_SHORT).show();
            verticalViewPager = (VerticalViewPager) findViewById(R.id.verticleViewPager);
            verticalViewPager.setAdapter(new VerticlePagerAdapter(GreenInfoActivity.this,dataList,1));

        }
    }



    }




