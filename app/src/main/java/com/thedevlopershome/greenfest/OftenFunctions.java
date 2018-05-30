package com.thedevlopershome.greenfest;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;

import static com.thefinestartist.utils.service.ServiceUtil.getSystemService;

public class OftenFunctions {

    Context context;
    public OftenFunctions(){

    }

    public OftenFunctions(Context context){
        this.context=context;

    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected(); }


}
