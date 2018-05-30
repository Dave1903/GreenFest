package com.thedevlopershome.greenfest;





import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.thefinestartist.Base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PukaarRegistrationActivity extends FragmentActivity implements RegistrationFragment.OnFragmentInteractionListener{

    private String phoneNumberString;
    RegistrationFragment registrationFragment;
    UserProfileFragment userProfileFragment;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pukaar_registration);
        userProfileFragment=new UserProfileFragment();
        registrationFragment=new RegistrationFragment();
         Base.initialize(this);
        sharedPreferences=getSharedPreferences("userData", Activity.MODE_PRIVATE);

        if (sharedPreferences.getInt("dataCheck", 0) == 1) {
            Bundle bundle=new Bundle();
            bundle.putString("pukkarId",sharedPreferences.getString("userregid","PUK001"));
            bundle.putString("userName",sharedPreferences.getString("username","pukkar"));
            bundle.putString("phoneno",sharedPreferences.getString("userphoneno","XXXXXXXXXX"));
            bundle.putString("clgName",sharedPreferences.getString("userclgname","NIT raipur"));
            bundle.putString("branch",sharedPreferences.getString("userbranch","NIL"));
            userProfileFragment.setArguments(bundle);
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragcontainer,userProfileFragment);
            fragmentTransaction.commit();

        }
       else {
            AccessToken accessToken = AccountKit.getCurrentAccessToken();

            if (accessToken != null) {
                if(!new OftenFunctions(PukaarRegistrationActivity.this).isNetworkAvailable()) {
                    Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragcontainer, registrationFragment);
                fragmentTransaction.commit();

            } else {
                phoneLogin(getCurrentFocus());
            }

        }    }

    public static int   APP_REQUEST_CODE = 99;

    public void phoneLogin(final View view) {
        final Intent intent = new Intent(PukaarRegistrationActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration
        // ..
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }






    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = "error in verification";
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            String accountKitId = account.getId();
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            phoneNumberString = phoneNumber.toString();
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("userphoneno",phoneNumberString);
                            editor.apply();
                            Toast.makeText(PukaarRegistrationActivity.this, phoneNumberString, Toast.LENGTH_SHORT).show();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.fragcontainer, registrationFragment);
                            fragmentTransaction.commit();
                        }

                        @Override
                        public void onError(final AccountKitError error) {
                            Toast.makeText(PukaarRegistrationActivity.this, "error:"+error.getErrorType(), Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0,10));

                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
                Toast.makeText(this, "successfully verified", Toast.LENGTH_SHORT).show();
            }

            // Surface the result to your user in an appropriate way.

        }
    }

    String baseUrl;


    @Override
    public void onFragmentInteraction(String name,String clgname,String branch) {

        String registrationId;
        try {
             registrationId = "PUK" + name.substring(0, 4).toUpperCase() + sharedPreferences.getString("userphoneno", "XXXXXXXXXX").substring(10, 13);
        }
        catch (IndexOutOfBoundsException e){
            registrationId = "PUK" + name.toUpperCase() + sharedPreferences.getString("userphoneno", "XXXXXXXXXX").substring(10, 13);

        }
        registrationId=registrationId.replaceAll(" ","_");

        baseUrl=createUrl(name,clgname,branch,sharedPreferences.getString("userphoneno","XXXXXXXXXX"),registrationId);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",name);
        editor.putString("userclgname",clgname);
        editor.putString("userbranch",branch);
        editor.putString("userregid",registrationId);
         editor.apply();
        if(!new OftenFunctions(PukaarRegistrationActivity.this).isNetworkAvailable()) {
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }
        else {
            SendRequest sendRequest = new SendRequest();
            sendRequest.execute();
        }
    }


    private class SendRequest extends AsyncTask<Void,Void,String> {

        ProgressDialog progressDialog;

        String uploadToServer(){
            StringBuilder content = new StringBuilder();
            try {

                URL url = new URL(baseUrl);
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



            }
            catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }  catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return content.toString();

        }

        @Override
        protected String doInBackground(Void... voids) {
            return uploadToServer();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(PukaarRegistrationActivity.this, "sending request...", "Please wait", true);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s.equals("sucess ")){
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt("dataCheck",1);
                editor.apply();

             Bundle bundle=new Bundle();
             bundle.putString("pukkarId",sharedPreferences.getString("userregid","PUK001"));
             bundle.putString("userName",sharedPreferences.getString("username","pukkar"));
             bundle.putString("phoneno",sharedPreferences.getString("userphoneno","XXXXXXXXXX"));
             bundle.putString("clgName",sharedPreferences.getString("userclgname","NIT raipur"));
             bundle.putString("branch",sharedPreferences.getString("userbranch","NIL"));
             UserProfileFragment userProfile=new UserProfileFragment();
             userProfile.setArguments(bundle);
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragcontainer,userProfile);
            fragmentTransaction.commit();

            }
            else
                if(s.equals("error"))
                {
                    Toast.makeText(PukaarRegistrationActivity.this, "server error", Toast.LENGTH_SHORT).show();
                }



        }
    }





    private String  createUrl(String name,String clgname,String branch,String phoneno,String regId){
        String requestUrl="http://gogreennitrr.org/pukkarRegestrationAPI.php?";
        requestUrl+="name="+name+"&clgname="+clgname+"&branch="+branch+"&phoneno="+phoneno+"&regid="+regId;
        requestUrl=requestUrl.replaceAll(" ","%20");
        return requestUrl;
    }



}


















