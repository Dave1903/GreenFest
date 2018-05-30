package com.thedevlopershome.greenfest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.health.PackageHealthStats;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class UserProfileFragment extends Fragment {


   TextView pukaarRegistrationId;
   TextView name;
   TextView phonenumber;
   TextView clgname;
   TextView branch;

   String pukkarId;
   String bundleName;
   String phoneNo;
   String bunclgName;
   String bundleBranch;


    public UserProfileFragment() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        pukkarId=getArguments().getString("pukkarId","puk0001");
        bundleName=getArguments().getString("userName","pukaar");
        phoneNo=getArguments().getString("phoneno","xxxxxxxxxx");
        bunclgName=getArguments().getString("clgName","NIT Raipur");
        bundleBranch=getArguments().getString("branch","NIl");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_user_profile, container, false);
        pukaarRegistrationId=(TextView)view.findViewById(R.id.textId);
        name=(TextView)view.findViewById(R.id.textView2);
        phonenumber=(TextView)view.findViewById(R.id.textView3);
        clgname=(TextView)view.findViewById(R.id.textView4);
        branch=(TextView)view.findViewById(R.id.textView6);

        pukaarRegistrationId.setText("USER ID: "+pukkarId);
        name.setText(bundleName);
        phonenumber.setText(phoneNo);
        clgname.setText(bunclgName);
        branch.setText(bundleBranch);


        return view;
    }




    @Override
    public void onDetach() {
        super.onDetach();

    }


}
