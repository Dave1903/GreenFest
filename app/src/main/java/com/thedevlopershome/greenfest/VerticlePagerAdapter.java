package com.thedevlopershome.greenfest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import static com.thefinestartist.utils.content.ContextUtil.startActivity;


public class VerticlePagerAdapter extends PagerAdapter {


    private ImageView fbButton;
    private ImageView profileImage;
    private TextView description;
    private TextView aboutTitle;
    private TextView findTitle;

     ArrayList<UserDataHolder> userData;

    Context mContext;
    LayoutInflater mLayoutInflater;
    int flag;

    public VerticlePagerAdapter(Context context,ArrayList<UserDataHolder> userData,int flag) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.flag=flag;
         this.userData=userData;
    }

    @Override
    public int getCount() {
        return  userData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.card_layout, container, false);

        profileImage=(ImageView)itemView.findViewById(R.id.imageView);
        aboutTitle=(TextView)itemView.findViewById(R.id.aboutTitle);
        findTitle=(TextView)itemView.findViewById(R.id.findTitle);
        description=(TextView)itemView.findViewById(R.id.devlopersDetail);
        fbButton=(ImageView)itemView.findViewById(R.id.facebookButton);

        description.setText(userData.get(position).description);
        if(flag==1) {
            Glide.with(mContext).load(userData.get(position).imageUrl).override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).into(profileImage);

        }
        else
            profileImage.setImageResource(R.drawable.pukkar);

        findTitle.setText(userData.get(position).findTitle);
        aboutTitle.setText(userData.get(position).aboutMe);
        container.addView(itemView);


     fbButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Uri uriUrl = Uri.parse(userData.get(position).fbUrl);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);    }
});

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }






}