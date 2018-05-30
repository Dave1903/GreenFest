package com.thedevlopershome.greenfest;


import android.graphics.Paint;
import android.net.Uri;

public class UserDataHolder {

   public   String aboutMe;
   public   String description;
   public   String imageUrl;
   public   String findTitle;
   public   String fbUrl;

     public UserDataHolder(){

     }

     public UserDataHolder(String aboutMe,String description,String imageUrl,String findTitle,String fbUrl){
         this.aboutMe=aboutMe;
         this.description=description;
         this.imageUrl=imageUrl;
         this.findTitle=findTitle;
         this.fbUrl=fbUrl;
     }


}



