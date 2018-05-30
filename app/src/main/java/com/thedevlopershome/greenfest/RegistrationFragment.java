package com.thedevlopershome.greenfest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;


public class RegistrationFragment extends Fragment {


    private MaterialEditText name;
    private MaterialEditText clgname;
    private MaterialEditText branchname;
    private TextView registerButton;


    private OnFragmentInteractionListener mListener;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view= inflater.inflate(R.layout.fragment_registration, container, false);
         name=(MaterialEditText)view.findViewById(R.id.username);
         clgname=(MaterialEditText)view.findViewById(R.id.clgname);
         branchname=(MaterialEditText)view.findViewById(R.id.userbranch);
         registerButton=(TextView)view.findViewById(R.id.requestButton);
         registerButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 mListener.onFragmentInteraction(name.getText().toString(),clgname.getText().toString(),branchname.getText().toString());
             }
         });

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String name,String clgname,String branch);
    }
}
